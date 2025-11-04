package com.muted987.cloudStorage.controller;

import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectoryResponse createFolder(@Valid @ModelAttribute PathParam pathParam,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.directoryService.createFolder(pathParam.path(), userDetails.getId());
    }

    //TODO: Заменить pathParam для валидации директории
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceResponse> getDirectory(@Valid @ModelAttribute PathParam pathParam,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.directoryService.getDirectory(pathParam.path(), userDetails.getId());
    }

}
