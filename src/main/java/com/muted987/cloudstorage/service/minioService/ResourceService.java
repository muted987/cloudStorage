package com.muted987.cloudStorage.service.minioService;

import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.exception.ResourceAlreadyExistsException;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.mapper.ResourceResponseMapper;
import com.muted987.cloudStorage.repository.MinioS3Repository;
import com.muted987.cloudStorage.utils.PathUtil;
import com.muted987.cloudStorage.utils.ZipUtil;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
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

    private final MinioS3Repository minioS3Repository;
    private final ResourceResponseMapper resourceResponseMapper;
    private final DirectoryService directoryService;


    /*
        Пока тестировал в постмане я обнаружил
        проблему со старым методом download.
        Может после того как перепишу его все изменится
        Если я создаю папку то я создаю объект с 0 размером(0byte)
        Но при загрузке папки с файлом разом этого не происходит
        и этот метод не передает ответ к папке
     */
    public ResourceResponse getResource(String requestPath,
                                        int id) {
        if (!isResourceExist(requestPath, id)) {
            throw new ResourceNotFoundException("Ресурс не найден");
        }
        String formattedPath = PathUtil.formatPath(id, requestPath);
        String parentPath = PathUtil.getParentPath(requestPath);
        String formattedParentPath = PathUtil.formatPath(id, parentPath);
        try {
            StatObjectResponse statObjectResponse = minioS3Repository.getObjectStat(formattedPath);
            if (statObjectResponse.object().endsWith("/")) {
                return this.resourceResponseMapper.toDirectoryResponse(statObjectResponse, formattedParentPath);
            } else {
                return this.resourceResponseMapper.toFileResponse(statObjectResponse, formattedParentPath);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<ResourceResponse> uploadResource(String requestPath,
                                                 MultipartFile resource,
                                                 int id) throws Exception {
        if (resource.getResource().getFilename() == null) {
            throw new RuntimeException();
        }
        String resourceFileFullName = resource.getResource().getFilename();
        if (isResourceExist(resourceFileFullName, id)) {
            throw new ResourceAlreadyExistsException("Файл уже существует");
        }
        String formattedUploadPath = PathUtil.formatPath(id, requestPath, resourceFileFullName);
        List<ResourceResponse> uploadedResources = new ArrayList<>();
        if (resourceFileFullName.contains("/")) {
            uploadedResources = this.directoryService.createParentFolders(resourceFileFullName, id);
        }
        ObjectWriteResponse objectWriteResponse = this.minioS3Repository.putObject(formattedUploadPath, resource);
        uploadedResources.add(this.resourceResponseMapper.toFileResponse(objectWriteResponse, PathUtil.formatPath(id, requestPath)));
        return uploadedResources;
    }

    public List<ResourceResponse> searchResources(String query,
                                                  int id) {
        List<ResourceResponse> resourceResponses = this.directoryService.getDirectoryRecursive("", id).stream()
                .filter(resourceResponse -> resourceResponse.getPath().concat(resourceResponse.getName()).contains(query.trim()))
                .toList();
        return resourceResponses.stream()
                .peek(resourceResponse -> {
                    resourceResponse.setPath(PathUtil.getParentPath(resourceResponse.getName()));
                    resourceResponse.setName(resourceResponse.getName().replace(PathUtil.getParentPath(resourceResponse.getName()), ""));
                })
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
                InputStream inputStream = this.minioS3Repository.getObject(formattedPath);
                return IOUtils.toByteArray(inputStream);
            }
            List<String> pathsForDownload = new ArrayList<>(resourceResponses.stream().
                    map(resourceResponse -> PathUtil.formatPath(id, resourceResponse.getPath().concat(resourceResponse.getName())))
                    .toList());
            pathsForDownload.add(0, formattedPath);
            Collections.reverse(pathsForDownload);
            for (String pathForDownload : pathsForDownload) {
                InputStream inputStream = this.minioS3Repository.getObject(pathForDownload);
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
        try {
            if (!isResourceExist(requestPath, id)) {
                throw new ResourceNotFoundException("Ресурс не найден");
            }
            List<ResourceResponse> resourceResponses = this.directoryService.getDirectoryRecursive(requestPath, id);
            String formattedPath = PathUtil.formatPath(id, requestPath);
            if (resourceResponses.isEmpty()) {
                this.minioS3Repository.removeObject(formattedPath);
            }
            List<String> pathsForDelete = new ArrayList<>(resourceResponses.stream()
                    .map(resourceResponse -> PathUtil.formatPath(id, resourceResponse.getPath().concat(resourceResponse.getName())))
                    .toList());
            pathsForDelete.add(0, formattedPath);
            Collections.reverse(pathsForDelete);
            for (String pathForDelete : pathsForDelete) {
                this.minioS3Repository.removeObject(pathForDelete);
            }

        } catch (Exception e) {
            log.warn("Исключение выброшено в методе deleteResource");
            throw new RuntimeException();
        }
    }

    public ResourceResponse moveResource(String from, String to,
                                         int id) {
        try {
            byte[] resourceFrom = downloadResource(from, id);
            MultipartFile multipartFile = new ByteArrayMultipartFile(resourceFrom, to.substring(to.lastIndexOf("/") + 1));
            List<ResourceResponse> resourceResponses = uploadResource(PathUtil.getParentPath(to), multipartFile, id);
            deleteResource(from, id);
            return resourceResponses.stream()
                    .filter(resourceResponse ->
                            to.equals(resourceResponse.getPath().concat(resourceResponse.getName())))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Ресурс не найден"));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isResourceExist(String path, int id) {
        return this.minioS3Repository.isObjectExist(PathUtil.formatPath(id, path));
    }

}
