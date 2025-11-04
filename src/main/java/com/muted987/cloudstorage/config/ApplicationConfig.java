package com.muted987.cloudStorage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.DirectoryResponseFactory;
import com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory.FileResponseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {


    @Bean
    public DirectoryResponseFactory directoryResponseFactory(){
        return new DirectoryResponseFactory();
    }

    @Bean
    public FileResponseFactory fileResponseFactory(){
        return new FileResponseFactory();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
