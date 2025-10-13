package com.muted987.cloudStorage.repository;

import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface S3Repository {

    void createBucket(String bucketName) throws Exception;
    boolean bucketExist(String bucketName) throws Exception;
    void createDirectory(String path) throws Exception;
    StatObjectResponse getObjectStat(String path) throws Exception;
    Iterable<Result<Item>> getListObjects(String prefix);
    Iterable<Result<Item>> getListObjectsRecursive(String prefix);
    void removeObject(String path) throws Exception;
    void putObject(String objectName, MultipartFile multipartFile) throws Exception;
    InputStream getObject(String path) throws Exception;
}
