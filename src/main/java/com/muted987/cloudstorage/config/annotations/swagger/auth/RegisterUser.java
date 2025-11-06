package com.muted987.cloudStorage.config.annotations.swagger.auth;

import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Registration method",
        description = "Registration method take credentials as a JSON object",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User credentials for registration",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RegisterDTO.class)
                )
        ),
        responses = {
                @ApiResponse(
                        description = "Registration success",
                        responseCode = "201",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = UserResponse.class)
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
                        description = "Username already taken",
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
public @interface RegisterUser {
}
