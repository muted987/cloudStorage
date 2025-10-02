package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.response.UserResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserInformation(@NotNull Authentication userDetails) {
        return new UserResponse(userDetails.getName());
    }

}
