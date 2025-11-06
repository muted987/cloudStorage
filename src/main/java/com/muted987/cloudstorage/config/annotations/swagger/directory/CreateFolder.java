package com.muted987.cloudStorage.config.annotations.swagger.directory;

import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

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
public @interface CreateFolder {
}
