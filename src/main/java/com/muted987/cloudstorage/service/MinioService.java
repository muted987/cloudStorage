package com.muted987.cloudStorage.service;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.DirectoryResponseFactory;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.FileResponseFactory;
import com.muted987.cloudStorage.repository.MinioS3Repository;
import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioS3Repository minioS3Repository;
    private final DirectoryResponseFactory directoryResponseFactory;
    private final FileResponseFactory fileResponseFactory;

    private final static String BASE_USER_FOLDER_NAME = "user-%s-files/";

    public FileResponse uploadFile(String path, MultipartFile multipartFile, int id) throws Exception{
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path).concat(multipartFile.getResource().getFilename());
        ObjectWriteResponse objectWriteResponse = minioS3Repository.putObject(fullPath, multipartFile);
        return fileResponseFactory.createResourceResponse(path, multipartFile.getResource().getFilename(), (int) multipartFile.getSize());
    }


    public DirectoryResponse createFolder(String path, int id) throws Exception {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path).concat("/");
        minioS3Repository.createDirectory(fullPath);
        return directoryResponseFactory.createResourceResponse(path, fullPath);
    }

    public List<ResourceResponse> getDirectory(String path, int id) {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        Iterable<Result<Item>> result = minioS3Repository.getListObjects(fullPath);
        return getResources(id, result, fullPath);
    }

    public void deleteResource(String path, int id) throws Exception {
        String fullPath = BASE_USER_FOLDER_NAME.formatted(id).concat(path);
        minioS3Repository.removeObject(fullPath);
    }

    public ResourceResponse moveResource(String fromPath, String toPath, int id) {
        return null;
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
                        DirectoryResponse directoryResponse = directoryResponseFactory.createResourceResponse(resourcePath, objectName);
                        directoryResponses.add(directoryResponse);
                    } else {
                        FileResponse fileResponse = fileResponseFactory.createResourceResponse(resourcePath, objectName, (int) object.size());
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
