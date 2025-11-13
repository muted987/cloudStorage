package com.muted987.cloudStorage.repository.s3Repository;

import com.muted987.cloudStorage.mapper.ItemInfoMapper;
import com.muted987.cloudStorage.service.minioService.ItemInfo;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MinioRepository implements S3Repository {


    private final ItemInfoMapper itemInfoMapper;
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
    public ItemInfo createDirectory(String path) {
        try {
            ObjectWriteResponse objectWriteResponse = this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(baseBucketName)
                            .object(path)
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .build());
            return this.itemInfoMapper.toItemInfo(objectWriteResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemInfo getObjectStat(String path) throws ErrorResponseException {
        try {
            StatObjectResponse statObjectResponse = this.minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(baseBucketName)
                            .object(path)
                            .build()
            );
            return this.itemInfoMapper.toItemInfo(statObjectResponse);
        } catch(ErrorResponseException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ItemInfo> getListObjects(String prefix) {
        Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(baseBucketName)
                        .prefix(prefix)
                        .build()
        );
        return this.itemInfoMapper.toItemInfoList(results, prefix);
    }

    @Override
    public List<ItemInfo> getListObjectsRecursive(String prefix) {
        Iterable<Result<Item>> results = this.minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(baseBucketName)
                        .prefix(prefix)
                        .recursive(true)
                        .build()
        );
        return this.itemInfoMapper.toItemInfoList(results, prefix);
    }


    @Override
    public void removeObject(String path) {
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(baseBucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemInfo putObject(String objectName, MultipartFile multipartFile) {

        try {
            this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(baseBucketName)
                            .object(objectName)
                            .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                            .contentType(multipartFile.getContentType())
                            .build()
            );
            return getObjectStat(objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getObject(String path) {
        try {
            return this.minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(baseBucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isObjectExist(String path) {
        try {
            getObjectStat(path);
            return true;
        } catch (ErrorResponseException e) {
            if (e.response().code() == HttpStatus.NOT_FOUND.value())
                return false;
            else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemInfo copyObject(String sourceObjectName, String copyObjectPath) {
        try {
            this.minioClient
                    .copyObject(
                            CopyObjectArgs.builder()
                                    .bucket(baseBucketName)
                                    .object(copyObjectPath)
                                    .source(
                                            CopySource.builder()
                                                    .object(sourceObjectName)
                                                    .bucket(baseBucketName)
                                                    .build()
                                    )
                                    .build()
                    );
            return getObjectStat(copyObjectPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
