package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.service.AuthService;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;
    private final DirectoryService directoryService;

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

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse loginUser(@RequestBody @Valid LoginDTO loginDTO,
                                  HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse){
        log.info("POST /api/auth/sign-in - Login user {}", loginDTO.username());
        return this.authService.loginUser(loginDTO, httpServletResponse, httpServletRequest);
    }

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        this.authService.logoutUser(httpServletRequest, httpServletResponse);
    }

}
