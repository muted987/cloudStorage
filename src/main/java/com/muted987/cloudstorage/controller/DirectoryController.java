package com.muted987.cloudStorage.controller;

import com.muted987.cloudStorage.controller.payload.PathParam;
import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Create directory method",
            description = "Create directory in user's storage",
            responses = {
                    @ApiResponse(
                            description = "Folder created with success",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DirectoryResponse.class)
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
                            description = "Parent folder doest not exist",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionMessage.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Directory already exist",
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

    @Operation(
            summary = "Get directory method",
            description = "Get all resource information such as path, name, size from user's directory",
            responses = {
                    @ApiResponse(
                            description = "Getting directory information with success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DirectoryResponse.class))
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
                            description = "Folder doest not exist",
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
    //TODO: Заменить pathParam для валидации директории
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
