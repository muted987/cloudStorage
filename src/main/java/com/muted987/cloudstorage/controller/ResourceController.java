package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.controller.payload.MoveParam;
import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.MinioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final MinioService minioService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResourceResponse> uploadFile(@NotNull(message = "Отсутствует файл")
                                             @RequestPart("object") MultipartFile multipartFile,
                                             @Valid @ModelAttribute PathParam pathParam,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return this.minioService.uploadFile(pathParam.path(), multipartFile, userDetails.getId());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@Valid @ModelAttribute PathParam pathParam,
                           @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        this.minioService.deleteResource(pathParam.path(), userDetails.getId());
    }

    @GetMapping("move")
    public ResourceResponse moveFile(@Valid @ModelAttribute MoveParam moveParam,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return this.minioService.moveResource(moveParam.from(), moveParam.to(), userDetails.getId());
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public InputStreamResource downloadResource(@Valid @ModelAttribute PathParam pathParam,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return new InputStreamResource(this.minioService.downloadResource(pathParam.path(), userDetails.getId()));
    }

    @GetMapping()
    public ResourceResponse getResource(@Valid @ModelAttribute PathParam pathParam,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return this.minioService.getResource(pathParam.path(), userDetails.getId());
    }


}
