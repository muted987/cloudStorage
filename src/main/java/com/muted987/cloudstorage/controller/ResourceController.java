package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.controller.payload.MoveParam;
import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.controller.payload.QueryParam;
import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "Resource controller",
        description = "Controller to work with resource in storage"
)
@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(
            summary = "Upload resource method",
            description = "Method to upload resource to user's folder",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User files to upload",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MultipartFile.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Resource uploaded with success",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ResourceResponse.class))
                            )
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource already exist",
                            responseCode = "409",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResourceResponse> uploadResource(@NotNull(message = "Отсутствует файл")
                                                 @RequestPart("object") MultipartFile multipartFile,
                                                 @Parameter(
                                                         description = "Path to new directory",
                                                         required = true
                                                 )
                                                 @Valid @ModelAttribute PathParam pathParam,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return this.resourceService.uploadResource(pathParam.path(), multipartFile, userDetails.getId());
    }

    @Operation(
            summary = "Deleting resource method",
            description = "Method to delete resource from user's folder",
            responses = {
                    @ApiResponse(
                            description = "Resource deleted with success",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Move resource method",
            description = "Method to move resource from path1(\"from\") to path2(\"to\")",
            responses = {
                    @ApiResponse(
                            description = "Resource moved with success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResourceResponse.class)
                            )

                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource on \"to\" path already exist",
                            responseCode = "409",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Download resource method",
            description = "Method to download file or archive(.zip)",
            responses = {
                    @ApiResponse(
                            description = "Resource downloaded with success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Get resource method",
            description = "Method to get path, name, size of a resource",
            responses = {
                    @ApiResponse(
                            description = "Getting resource with success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResourceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceResponse getResource(@Parameter(
                                                    description = "Path to new directory",
                                                    required = true
                                            )
                                        @Valid @ModelAttribute PathParam pathParam,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.resourceService.getResource(pathParam.path(), userDetails.getId());
    }

    @Operation(
            summary = "Search resource method",
            description = "Method to search resource in entire storage",
            responses = {
                    @ApiResponse(
                            description = "Resource founded with success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResourceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized user request error",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Eternal error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    )
            }
    )
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
