package com.muted987.cloudStorage.controller;

import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.MinioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {

    private final MinioService minioService;

    public DirectoryController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectoryResponse createFolder(@Valid @ModelAttribute PathParam pathParam,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return minioService.createFolder(pathParam.path(), userDetails.getId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceResponse> getDirectory(@Valid @ModelAttribute PathParam pathParam,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        return minioService.getDirectory(pathParam.path(), userDetails.getId());
    }

}
