package com.muted987.cloudStorage.service.minioService;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.exception.ParentFolderNotExist;
import com.muted987.cloudStorage.exception.ResourceAlreadyExistsException;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.mapper.ResourceResponseMapper;
import com.muted987.cloudStorage.repository.MinioS3Repository;
import com.muted987.cloudStorage.utils.PathUtil;
import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    private final MinioS3Repository minioS3Repository;
    private final ResourceResponseMapper resourceResponseMapper;

    public List<ResourceResponse> getDirectory(String requestPath, int id) {
        String formattedPath = PathUtil.formatPath(id, requestPath);
        if (!isFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе getDirectory");
            throw new ResourceNotFoundException("Папка не существует");
        }
        Iterable<Result<Item>> result = minioS3Repository.getListObjects(formattedPath);
        return getResourceResponses(result, formattedPath);
    }

    public List<ResourceResponse> getDirectoryRecursive(String requestPath, int id) {
        String refactoredPath = PathUtil.formatPath(id, requestPath);
        if (!isFolderExist(requestPath, id)) {
            log.warn("Исключение выброшено в методе getDirectoryRecursive");
            throw new ResourceNotFoundException("Папка не существует");
        }
        Iterable<Result<Item>> result = minioS3Repository.getListObjectsRecursive(refactoredPath);
        return getResourceResponses(result, refactoredPath);
    }


    public DirectoryResponse createFolder(String path, int id) {
        if (!isParentFolderExist(path, id)) {
            log.warn("Исключение выброшено в методе createFolder");
            throw new ParentFolderNotExist("Родительская папка не существует");
        }
        if (isFolderExist(path, id)) {
            log.warn("Исключение выброшено в методе createFolder");
            throw new ResourceAlreadyExistsException("Папка уже существует");
        }
        String formattedPath = PathUtil.formatPath(id, path);
        ObjectWriteResponse result;
        try {
            result = minioS3Repository.createDirectory(formattedPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resourceResponseMapper.toDirectoryResponse(result, formattedPath);
    }

    private boolean isParentFolderExist(String path, int id) {
        String parentPath = PathUtil.getParentPath(path);
        String parentPathOfParentPath = PathUtil.getParentPath(parentPath);
        if (parentPathOfParentPath.isEmpty()) {
            return true;
        }
        return getDirectory(parentPathOfParentPath, id).stream()
                .anyMatch(resourceResponse -> resourceResponse.getPath().concat(resourceResponse.getName()).equals(parentPath));
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

    private @NotNull List<ResourceResponse> getResourceResponses(Iterable<Result<Item>> result, String formattedPath) {
        List<ResourceResponse> resourceResponses = new ArrayList<>();
        for (Result<Item> item : result) {
            try {
                Item object = item.get();
                if (PathUtil.reformatPath(object.objectName()).isEmpty() || object.objectName().equals(formattedPath)) {
                    continue;
                }
                if (object.objectName().endsWith("/")) {
                    resourceResponses.add(resourceResponseMapper.toDirectoryResponse(object, formattedPath));
                } else {
                    resourceResponses.add(resourceResponseMapper.toFileResponse(object, formattedPath));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resourceResponses;
    }

    private boolean isFolderExist(String path, int id) {
        String parentPath;
        if (!path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf("/") + 1);
            parentPath = path;
        } else {
            parentPath = PathUtil.getParentPath(path);
        }
        if (isRootFolder(path)) {
            return true;
        }
        String finalPath = path;
        return getDirectory(parentPath, id).stream()
                .anyMatch(resourceResponse -> resourceResponse.getPath().concat(resourceResponse.getName()).equals(finalPath));
    }

    private boolean isRootFolder(String path) {
        return "".equals(path);
    }
}
