package com.muted987.cloudStorage.repository;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Repository
public class MinioS3Repository implements S3Repository {


    @Value("${minio.base-bucket-name}")
    private String baseBucketName;

    private final MinioClient minioClient;

    @Override
    public void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    @Override
    public boolean bucketExist(String bucketName) throws Exception {
        return minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    @Override
    public void createDirectory(String fullPath) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(fullPath)
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build());
    }

    @Override
    public StatObjectResponse getObjectStat(String fullPath) throws Exception {
        return minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(fullPath)
                        .build()
        );
    }

    @Override
    public Iterable<Result<Item>> getListObjects(String prefix) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(baseBucketName)
                        .prefix(prefix)
                        .build()
        );
    }

    @Override
    public void removeObject(String fullPath) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(fullPath)
                        .build()
        );
    }

    public ObjectWriteResponse putObject(String objectName, MultipartFile multipartFile) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(objectName)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build()
        );
    }
}
