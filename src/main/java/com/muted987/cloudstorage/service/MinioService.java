package com.muted987.cloudStorage.service;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.DirectoryResponseFactory;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.FileResponseFactory;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.repository.MinioS3Repository;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioS3Repository minioS3Repository;
    private final DirectoryResponseFactory directoryResponseFactory;
    private final FileResponseFactory fileResponseFactory;

    private final static String BASE_USER_FOLDER_NAME = "user-%s-files/";

    public List<ResourceResponse> uploadFile(String path, MultipartFile multipartFile, int id) throws Exception {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path).concat(multipartFile.getResource().getFilename());
        this.minioS3Repository.putObject(fullPath, multipartFile);
        return getDirectory(path, id);
    }

    public DirectoryResponse createFolder(String path, int id) throws Exception {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path).concat("/");
        this.minioS3Repository.createDirectory(fullPath);
        return this.directoryResponseFactory.createResourceResponse(path, fullPath);
    }

    public List<ResourceResponse> getDirectory(String path, int id) {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        Iterable<Result<Item>> result = this.minioS3Repository.getListObjects(fullPath);
        return getResources(id, result, fullPath);
    }

    public void deleteResource(String path, int id) {
        try {
            List<String> directoriesToDelete = getDirectoriesFullPathList(path, id);
            if (!directoriesToDelete.isEmpty()) {
                for (String directoryPath : directoriesToDelete) {
                    this.minioS3Repository.removeObject(directoryPath);
                }
            }
        }  catch (ErrorResponseException e) {
            if (e.response().code() == HttpStatus.NOT_FOUND.value())
                throw new ResourceNotFoundException("Ресурс не найден");
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getDirectoriesFullPathList(String path, int id) throws Exception {
        Iterable<Result<Item>> directories = getDirectoryRecursive(path, id);
        List<String> resultDirectories = new ArrayList<>();
        for (Result<Item> result : directories) {
            Item item = result.get();
            resultDirectories.add(item.objectName());
        }
        return resultDirectories;
    }

    private Iterable<Result<Item>> getDirectoryRecursive(String path, int id) {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        return this.minioS3Repository.getListObjectsRecursive(fullPath);
    }

    public ResourceResponse moveResource(String fromPath, String toPath, int id) {
        return null;
    }

    public ResourceResponse getResource(String path, int id) {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        try {
            StatObjectResponse response = this.minioS3Repository.getObjectStat(fullPath);
            String objectName = response.object().substring(fullPath.length());
            String resourcePath = fullPath.substring(BASE_USER_FOLDER_NAME.formatted(id).length());
            if (objectName.endsWith("/")) {
                return this.directoryResponseFactory.createResourceResponse(resourcePath, objectName);
            } else {
                return this.fileResponseFactory.createResourceResponse(resourcePath, objectName, (int) response.size());
            }
        } catch (ErrorResponseException e) {
            if (e.response().code() == HttpStatus.NOT_FOUND.value())
                throw new ResourceNotFoundException("Ресурс не найден");
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public InputStream downloadResource(String path, int id) throws Exception {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        return this.minioS3Repository.getObject(fullPath);
    }

    @NotNull
    private List<ResourceResponse> getResources(int id, Iterable<Result<Item>> result, String fullPath) {
        List<ResourceResponse> directoryResponses = new ArrayList<>();
        for (Result<Item> item : result) {
            try {
                Item object = item.get();
                if (!object.objectName().equals(fullPath)) {
                    String objectName = object.objectName().substring(fullPath.length());
                    String resourcePath = fullPath.substring(BASE_USER_FOLDER_NAME.formatted(id).length());
                    if (objectName.endsWith("/")) {
                        DirectoryResponse directoryResponse = this.directoryResponseFactory.createResourceResponse(resourcePath, objectName);
                        directoryResponses.add(directoryResponse);
                    } else {
                        FileResponse fileResponse = this.fileResponseFactory.createResourceResponse(resourcePath, objectName, (int) object.size());
                        directoryResponses.add(fileResponse);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return directoryResponses;
    }
}
