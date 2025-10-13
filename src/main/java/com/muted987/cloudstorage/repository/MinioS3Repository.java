package com.muted987.cloudStorage.repository;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MinioS3Repository implements S3Repository {


    @Value("${minio.base-bucket-name}")
    private String baseBucketName;

    private final MinioClient minioClient;

    @Override
    public void createBucket(String bucketName) throws Exception {
        this.minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    @Override
    public boolean bucketExist(String bucketName) throws Exception {
        return this.minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
    }

    @Override
    public void createDirectory(String path) throws Exception {
        this.minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(path)
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build());
    }

    @Override
    public StatObjectResponse getObjectStat(String path) throws Exception {
        return this.minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(path)
                        .build()
        );
    }

    @Override
    public Iterable<Result<Item>> getListObjects(String prefix) {
        return this.minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(baseBucketName)
                        .prefix(prefix)
                        .build()
        );
    }

    @Override
    public Iterable<Result<Item>> getListObjectsRecursive(String prefix) {
        return this.minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(baseBucketName)
                        .prefix(prefix)
                        .recursive(true)
                        .build()
        );
    }

    @Override
    public void removeObject(String path) throws Exception {
        this.minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(path)
                        .build()
        );
    }

    @Override
    public void putObject(String objectName, MultipartFile multipartFile) throws Exception {
        this.minioClient.putObject(
            PutObjectArgs.builder()
                    .bucket(baseBucketName)
                    .object(objectName)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build()
        );
    }

    @Override
    public InputStream getObject(String path) throws Exception {
        return this.minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(baseBucketName)
                        .object(path)
                        .build()
        );
    }
}
