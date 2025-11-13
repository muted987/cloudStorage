package com.muted987.cloudStorage.config;

import com.muted987.cloudStorage.repository.s3Repository.MinioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketInitializer {


    @Value("${minio.base-bucket-name}")
    private String baseBucketName;

    private final MinioRepository minioRepository;

    @PostConstruct
    public void createBaseBucket() throws Exception {
        if (!minioRepository.bucketExist(baseBucketName)) {
            minioRepository.createBucket(baseBucketName);
        }
    }

}
