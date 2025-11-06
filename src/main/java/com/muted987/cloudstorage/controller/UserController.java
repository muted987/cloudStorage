package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.config.annotations.swagger.GetUser;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
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

    @GetUser
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@NotNull @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new UserResponse(userDetails.getId(), userDetails.getUsername());
    }

}
