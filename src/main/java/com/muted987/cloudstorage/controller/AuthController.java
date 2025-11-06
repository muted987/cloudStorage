package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.config.annotations.swagger.auth.LoginUser;
import com.muted987.cloudStorage.config.annotations.swagger.auth.LogoutUser;
import com.muted987.cloudStorage.config.annotations.swagger.auth.RegisterUser;
import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.service.AuthService;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Auth controller",
        description = "Authorization methods"
)
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;
    private final DirectoryService directoryService;


    @RegisterUser
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody @Valid RegisterDTO registerDTO,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {
        log.info("POST /api/auth/sign-up - Registration user {}", registerDTO.username());
        UserResponse userResponse = this.authService.registerUser(registerDTO, httpServletResponse, httpServletRequest);
        this.directoryService.createRootFolder(userResponse.id());
        return userResponse;
    }

    @LoginUser
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse loginUser(@RequestBody @Valid LoginDTO loginDTO,
                                  HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse){
        log.info("POST /api/auth/sign-in - Login user {}", loginDTO.username());
        return this.authService.loginUser(loginDTO, httpServletResponse, httpServletRequest);
    }

    @LogoutUser
    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        this.authService.logoutUser(httpServletRequest, httpServletResponse);
    }

}
