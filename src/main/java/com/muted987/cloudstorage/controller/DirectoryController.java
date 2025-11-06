package com.muted987.cloudStorage.controller;

import com.muted987.cloudStorage.config.annotations.swagger.directory.CreateFolder;
import com.muted987.cloudStorage.config.annotations.swagger.directory.GetDirectory;
import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Directory controller",
        description = "Controller to work with directories in storage"
)
@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @CreateFolder
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectoryResponse createFolder(@Parameter(
                                                      description = "Path to new directory",
                                                      required = true
                                              )
                                          @Valid @ModelAttribute PathParam pathParam,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.directoryService.createFolder(pathParam.path(), userDetails.getId());
    }

    //TODO: Заменить pathParam для валидации директории
    @GetDirectory
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceResponse> getDirectory(@Parameter(
                                                           description = "Path to new directory",
                                                           required = true
                                                   )
                                                   @Valid @ModelAttribute PathParam pathParam,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.directoryService.getDirectory(pathParam.path(), userDetails.getId());
    }

}
