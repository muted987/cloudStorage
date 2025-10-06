package com.muted987.cloudStorage.repository;

import io.minio.Result;
import io.minio.messages.Item;

public interface S3Repository {

    void createBucket(String bucketName) throws Exception;
    boolean bucketExist(String bucketName) throws Exception;
    void createDirectory(String fullPath) throws Exception;
    Object getObjectStat(String fullPath) throws Exception;
    Iterable<Result<Item>> getListObjects(String prefix);
    void removeObject(String fullPath) throws Exception;
}
