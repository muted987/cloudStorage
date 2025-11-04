package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.controller.payload.MoveParam;
import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.controller.payload.QueryParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResourceResponse> uploadResource(@NotNull(message = "Отсутствует файл")
                                                 @RequestPart("object") MultipartFile multipartFile,
                                                 @Valid @ModelAttribute PathParam pathParam,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return this.resourceService.uploadResource(pathParam.path(), multipartFile, userDetails.getId());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@Valid @ModelAttribute PathParam pathParam,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        this.resourceService.deleteResource(pathParam.path(), userDetails.getId());
    }

    @GetMapping("move")
    @ResponseStatus(HttpStatus.OK)
    public ResourceResponse moveResource(@Valid @ModelAttribute MoveParam moveParam,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.moveResource(moveParam.from(), moveParam.to(), userDetails.getId());
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> downloadResource(@Valid @ModelAttribute PathParam pathParam,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        byte[] zipBytes = this.resourceService.downloadResource(pathParam.path(), userDetails.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resources.zip\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(zipBytes);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceResponse getResource(@Valid @ModelAttribute PathParam pathParam,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.getResource(pathParam.path(), userDetails.getId());
    }

    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceResponse> searchResource(@Valid @ModelAttribute QueryParam queryParam,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.searchResources(queryParam.query(), userDetails.getId());
    }


}
