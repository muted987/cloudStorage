package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthService authService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody @Valid RegisterDTO registerDTO,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse){
        log.info("POST /api/auth/sign-up - Registration user {}", registerDTO.username());
        return this.authService.registerUser(registerDTO, httpServletResponse, httpServletRequest);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDTO loginDTO,
                                       HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse){
        log.info("POST /api/auth/sign-in - Login user {}", loginDTO.username());
        return new ResponseEntity<>(this.authService.loginUser(loginDTO, httpServletResponse, httpServletRequest), HttpStatus.OK);
    }

}
