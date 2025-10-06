package com.muted987.cloudStorage.config;

import com.muted987.cloudStorage.repository.MinioS3Repository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketInitializer {


    @Value("${minio.base-bucket-name}")
    private String baseBucketName;

    private final MinioS3Repository minioS3Repository;

    @PostConstruct
    public void createBaseBucket() throws Exception {
        if (!minioS3Repository.bucketExist(baseBucketName)) {
            minioS3Repository.createBucket(baseBucketName);
        }
    }

}
