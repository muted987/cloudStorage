package com.muted987.cloudStorage.service.minioService;

import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.exception.ResourceAlreadyExistsException;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.mapper.resourceResponseMapper.ResourceResponseMapper;
import com.muted987.cloudStorage.repository.s3Repository.MinioRepository;
import com.muted987.cloudStorage.utils.PathUtil;
import com.muted987.cloudStorage.utils.ZipUtil;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final MinioRepository minioRepository;
    private final ResourceResponseMapper resourceResponseMapper;
    private final DirectoryService directoryService;


    public ResourceResponse getResource(String requestPath,
                                        int id) throws ErrorResponseException {
        if (!isResourceExist(requestPath, id)) {
            throw new ResourceNotFoundException("Ресурс не найден");
        }

        String formattedPath = PathUtil.formatPath(id, requestPath);

        ItemInfo itemInfo = minioRepository.getObjectStat(formattedPath);

        return this.resourceResponseMapper.toResourceResponse(itemInfo);
    }

    public List<ResourceResponse> uploadResources(String requestPath,
                                                  MultipartFile[] resources,
                                                  int id) {
        List<ResourceResponse> uploadedResources = new ArrayList<>();
        for (MultipartFile resource : resources) {
            uploadedResources.addAll(uploadResource(requestPath, resource, id));
        }
        return uploadedResources;
    }

    private List<ResourceResponse> uploadResource(String requestPath, MultipartFile resource, int id) {
        if (resource.getResource().getFilename() == null) {
            throw new RuntimeException();
        }

        String resourceFileFullName = resource.getResource().getFilename();
        String formattedUploadPath = PathUtil.formatPath(id, requestPath, resourceFileFullName);

        if (isResourceExist(formattedUploadPath, id)) {
            throw new ResourceAlreadyExistsException("Файл уже существует");
        }

        List<ResourceResponse> uploadedResources = new ArrayList<>();
        if (resourceFileFullName.contains("/")) {
            uploadedResources = this.directoryService.createParentFolders(resourceFileFullName, id);
        }

        ItemInfo itemInfo = this.minioRepository.putObject(formattedUploadPath, resource);

        uploadedResources.add(this.resourceResponseMapper.toResourceResponse(itemInfo));

        return uploadedResources;
    }

    public List<ResourceResponse> searchResources(String query,
                                                  int id) {
        return this.directoryService.getDirectoryRecursive("", id).stream()
                .filter(resourceResponse -> resourceResponse.getPath().concat(resourceResponse.getName()).contains(query.trim()))
                .toList();
    }

    public byte[] downloadResource(String requestPath,
                                   int id) {
        if (!isResourceExist(requestPath, id)) {
            throw new ResourceNotFoundException("Ресурс не найден");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            List<ResourceResponse> resourceResponses = this.directoryService.getDirectoryRecursive(requestPath, id);
            String formattedPath = PathUtil.formatPath(id, requestPath);

            if (resourceResponses.isEmpty()) {
                InputStream inputStream = this.minioRepository.getObject(formattedPath);
                return IOUtils.toByteArray(inputStream);
            }

            List<String> pathsForDownload = new ArrayList<>(resourceResponses.stream().
                    map(resourceResponse -> PathUtil.formatPath(id, resourceResponse.getPath().concat(resourceResponse.getName())))
                    .toList());
            pathsForDownload.add(0, formattedPath);
            Collections.reverse(pathsForDownload);

            for (String pathForDownload : pathsForDownload) {
                InputStream inputStream = this.minioRepository.getObject(pathForDownload);
                ZipUtil.addStreamToZip(zos, inputStream, PathUtil.reformatPath(pathForDownload));
            }

            zos.finish();

            return baos.toByteArray();

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteResource(String requestPath,
                               int id) {
        if (!isResourceExist(requestPath, id)) {
            throw new ResourceNotFoundException("Ресурс не найден");
        }

        List<ResourceResponse> resourceResponses = this.directoryService.getDirectoryRecursive(requestPath, id);
        String formattedPath = PathUtil.formatPath(id, requestPath);

        if (resourceResponses.isEmpty()) {
            this.minioRepository.removeObject(formattedPath);
        }

        List<String> pathsForDelete = new ArrayList<>(resourceResponses.stream()
                .map(resourceResponse -> PathUtil.formatPath(id, resourceResponse.getPath().concat(resourceResponse.getName())))
                .toList());
        pathsForDelete.add(0, formattedPath);
        Collections.reverse(pathsForDelete);

        for (String pathForDelete : pathsForDelete) {
            this.minioRepository.removeObject(pathForDelete);
        }
    }

    public ResourceResponse moveResource(String from, String to,
                                         int id) {
        String formattedFromPath = PathUtil.formatPath(id, from);
        String formattedToPath = PathUtil.formatPath(id, to);
        if (!isResourceExist(from, id)) {
            throw new ResourceNotFoundException("Ресурс не существует");
        }
        if (isResourceExist(to, id)) {
            throw new ResourceAlreadyExistsException("Ресурс уже существует");
        }
        ItemInfo itemInfo = this.minioRepository.copyObject(formattedFromPath, formattedToPath);

        List<ResourceResponse> fullFromDirectory = this.directoryService.getDirectoryRecursive(from, id);
        if (fullFromDirectory.isEmpty()) {
            this.deleteResource(from, id);
            return this.resourceResponseMapper.toResourceResponse(itemInfo);
        }
        List<String> pathsToCopyTo = fullFromDirectory.stream()
                .map(resourceResponse -> {
                    String newPath = resourceResponse.getPath().replace(from, to);
                    return newPath.concat(resourceResponse.getName());
                })
                .toList();

        List<String> pathsToCopyFrom = fullFromDirectory.stream()
                .map(resourceResponse -> resourceResponse.getPath().concat(resourceResponse.getName()))
                .toList();

        for (int i = 0; i < fullFromDirectory.size(); i++) {
            this.minioRepository.copyObject(PathUtil.formatPath(id, pathsToCopyFrom.get(i)), PathUtil.formatPath(id, pathsToCopyTo.get(i)));
            deleteResource(pathsToCopyFrom.get(i), id);
        }

        this.deleteResource(from, id);
        return this.resourceResponseMapper.toResourceResponse(itemInfo);

    }

    private boolean isResourceExist(String path, int id) {
        return this.minioRepository.isObjectExist(PathUtil.formatPath(id, path));
    }

}
