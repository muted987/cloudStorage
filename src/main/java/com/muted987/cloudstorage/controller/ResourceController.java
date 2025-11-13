package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.config.annotations.swagger.resource.DeleteResource;
import com.muted987.cloudStorage.config.annotations.swagger.resource.DownloadResource;
import com.muted987.cloudStorage.config.annotations.swagger.resource.GetResource;
import com.muted987.cloudStorage.config.annotations.swagger.resource.MoveResource;
import com.muted987.cloudStorage.config.annotations.swagger.resource.SearchResource;
import com.muted987.cloudStorage.config.annotations.swagger.resource.UploadResource;
import com.muted987.cloudStorage.controller.payload.MoveParam;
import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.controller.payload.QueryParam;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.ResourceService;
import io.minio.errors.ErrorResponseException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "Resource controller",
        description = "Controller to work with resource in storage"
)
@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @UploadResource
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResourceResponse> uploadResource(@Parameter(
                                                         description = "Path to new directory",
                                                         required = true
                                                 )
                                                 @Valid @ModelAttribute PathParam pathParam,
                                                 @NotNull(message = "Отсутствует файл")
                                                 @RequestPart("object") MultipartFile[] resources,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.uploadResources(pathParam.path(), resources, userDetails.getId());
    }

    @DeleteResource
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@Parameter(
                                           description = "Path to new directory",
                                           required = true
                                   )
                               @Valid @ModelAttribute PathParam pathParam,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        this.resourceService.deleteResource(pathParam.path(), userDetails.getId());
    }

    @MoveResource
    @GetMapping("move")
    @ResponseStatus(HttpStatus.OK)
    public ResourceResponse moveResource(@Parameter(
                                                     description = "Paths from and to moving resources",
                                                     required = true
                                             )
                                         @Valid @ModelAttribute MoveParam moveParam,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.moveResource(moveParam.from(), moveParam.to(), userDetails.getId());
    }

    @DownloadResource
    @GetMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> downloadResource(@Parameter(
                                                               description = "Path to new directory",
                                                               required = true
                                                       )
                                                   @Valid @ModelAttribute PathParam pathParam,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        byte[] zipBytes = this.resourceService.downloadResource(pathParam.path(), userDetails.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resources.zip\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(zipBytes);

    }

    @GetResource
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceResponse getResource(@Parameter(
                                                    description = "Path to new directory",
                                                    required = true
                                            )
                                        @Valid @ModelAttribute PathParam pathParam,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws ErrorResponseException {
        return this.resourceService.getResource(pathParam.path(), userDetails.getId());
    }

    @SearchResource
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceResponse> searchResource(@Parameter(
                                                             description = "Query for searching resource",
                                                             required = true
                                                     )
                                                 @Valid @ModelAttribute QueryParam queryParam,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.searchResources(queryParam.query(), userDetails.getId());
    }


}
