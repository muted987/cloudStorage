package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "User controller",
        description = "Controller to get information about authorized user"
)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Operation(
            summary = "Get user method",
            description = "Getting user method from AuthenticationPrincipal",
            responses = {
                    @ApiResponse(
                            description = "Getting user success",
                            responseCode = "204"
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
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserInformation(@NotNull @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new UserResponse(userDetails.getId(), userDetails.getUsername());
    }

}
