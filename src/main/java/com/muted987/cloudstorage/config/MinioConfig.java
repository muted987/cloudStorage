package com.muted987.cloudStorage.config;


import com.muted987.cloudStorage.repository.S3Repository;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Value("${minio.secret-key}")
    private String minioSecretKey;

    @Value("${minio.base-bucket-name}")
    private String baseBucketName;


    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .credentials(minioAccessKey, minioSecretKey)
                .endpoint(minioUrl)
                .build();
    }
}
