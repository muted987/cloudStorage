package com.muted987.cloudStorage.repository.s3Repository;

import com.muted987.cloudStorage.service.minioService.ItemInfo;
import io.minio.errors.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface S3Repository {

    void createBucket(String bucketName) throws Exception;
    boolean bucketExist(String bucketName) throws Exception;
    ItemInfo createDirectory(String path);
    ItemInfo getObjectStat(String path) throws ErrorResponseException;
    List<ItemInfo> getListObjects(String prefix);
    List<ItemInfo> getListObjectsRecursive(String prefix);
    void removeObject(String path);
    ItemInfo putObject(String objectName, MultipartFile multipartFile);
    InputStream getObject(String path);
    boolean isObjectExist(String path);
    ItemInfo copyObject(String copyObjectPath, String sourceObjectName);
}
