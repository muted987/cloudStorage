package com.muted987.cloudStorage.configuration;


import com.redis.testcontainers.RedisContainer;
import io.minio.MinioClient;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@TestConfiguration()
@Testcontainers
public class TestConfig {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");
                    //.withInitScript("db/migration/V1_1_1__BasicUserManagerSchema.sql")

    @Container
    private static final MinIOContainer minioContainer = new MinIOContainer("minio/minio:latest");

    @Container
    private static final RedisContainer redisContainer = new RedisContainer("redis:latest");

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        redisContainer.start();
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisContainer.getHost());
        config.setPort(redisContainer.getFirstMappedPort());
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public DataSource dataSource() {
        postgreSQLContainer.start();
        return DataSourceBuilder.create()
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .build();
    }

    @Bean
    @Primary
    public MinioClient testMinioClient() {
        minioContainer.start();
        return MinioClient.builder()
                .endpoint(minioContainer.getS3URL())
                .credentials(minioContainer.getUserName(), minioContainer.getPassword())
                .build();
    }

}
