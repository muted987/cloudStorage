package com.muted987.cloudStorage.config.annotations.swagger.resource;

import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
public @interface MoveResource {
}
