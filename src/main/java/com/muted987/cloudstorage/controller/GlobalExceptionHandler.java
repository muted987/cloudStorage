package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.exception.ParentFolderNotExist;
import com.muted987.cloudStorage.exception.ResourceAlreadyExistsException;
import com.muted987.cloudStorage.exception.ResourceNotFoundException;
import com.muted987.cloudStorage.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleFailedAuthenticationException(Exception exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            ParentFolderNotExist.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(Exception exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler({
            ResourceAlreadyExistsException.class,
            UserAlreadyExistException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionMessage handleAlreadyExistsException(Exception exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleNotValidException(MethodArgumentNotValidException exception) {
        List<ExceptionMessage> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String message = error.getDefaultMessage();
            errors.add(
                    ExceptionMessage.builder()
                            .message(message)
                            .build()
            );
        });
        return errors.getFirst();
    }


    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleException() {
        return ExceptionMessage.builder()
                .message("unexpected exception")
                .build();
    }

}