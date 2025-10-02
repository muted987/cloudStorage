package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            UserAlreadyExistException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleFailedAuthenticationException(Exception exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleException(Exception exception) {
        log.error("Exception handling caused by {}", exception.getCause().toString());
        return ExceptionMessage.builder()
                .message("unexpected exception")
                .build();
    }

}