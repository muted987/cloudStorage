package com.muted987.cloudStorage.service.minioService;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.exception.ParentFolderNotExist;
import com.muted987.cloudStorage.exception.ResourceAlreadyExistsException;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.mapper.resourceResponseMapper.ResourceResponseMapper;
import com.muted987.cloudStorage.repository.s3Repository.S3Repository;
import com.muted987.cloudStorage.utils.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    private final S3Repository minioRepository;
    private final ResourceResponseMapper resourceResponseMapper;

    public List<ResourceResponse> getDirectory(String requestPath, int id) {
        String formattedPath = PathUtil.formatPath(id, requestPath);

        if (!isFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе getDirectory");
            throw new ResourceNotFoundException("Папка не существует");
        }

        List<ItemInfo> result = this.minioRepository.getListObjects(formattedPath);

        return this.resourceResponseMapper.toResourceResponseList(result);
    }

    public List<ResourceResponse> getDirectoryRecursive(String requestPath, int id) {
        String formattedPath = PathUtil.formatPath(id, requestPath);

        if (!isFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе getDirectoryRecursive");
            throw new ResourceNotFoundException("Папка не существует");
        }

        List<ItemInfo> result = this.minioRepository.getListObjectsRecursive(formattedPath);

        return this.resourceResponseMapper.toResourceResponseList(result);
    }

    public void createRootFolder(int id){
        String rootFolderPath = PathUtil.formatPath(id, "");

        if (isFolderExist(rootFolderPath, id)) {
            log.warn("Исключение выброшено в методе createRootFolder");
            throw new ResourceAlreadyExistsException("Папка уже существует");
        }

        try {
            this.minioRepository.createDirectory(rootFolderPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DirectoryResponse createFolder(String requestPath, int id) {

        if (!isParentFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе createFolder");
            throw new ParentFolderNotExist("Родительская папка не существует");
        }

        if (isFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе createFolder");
            throw new ResourceAlreadyExistsException("Папка уже существует");
        }

        String formattedPath = PathUtil.formatPath(id, requestPath);
        ItemInfo itemInfo = this.minioRepository.createDirectory(formattedPath);
        return (DirectoryResponse) this.resourceResponseMapper.toResourceResponse(itemInfo);
    }

    public List<ResourceResponse> createParentFolders(String path, int id) {
        List<String> folderPaths = PathUtil.getParentPaths(path.substring(0, path.lastIndexOf("/") + 1));
        List<ResourceResponse> resourceResponses = new ArrayList<>();

        for (String folderPath : folderPaths) {
            try {
                resourceResponses.add(createFolder(folderPath, id));
            } catch (ResourceAlreadyExistsException ignored) {
            } catch (ParentFolderNotExist e) {
                throw new ParentFolderNotExist(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return resourceResponses;
    }

    private boolean isFolderExist(String path, int id) {
        if (isRootFolder(path)) {
            return true;
        }
        return this.minioRepository.isObjectExist(PathUtil.formatPath(id, path));
    }

    private boolean isParentFolderExist(String path, int id) {
        String parentPath = PathUtil.getParentPath(path);
        return isFolderExist(parentPath, id);
    }

    private boolean isRootFolder(String path) {
        return "".equals(path);
    }
}
