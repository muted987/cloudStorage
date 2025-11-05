package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.ExceptionMessage;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.service.AuthService;
import com.muted987.cloudStorage.service.minioService.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Login method",
            description = "Login method take credentials as a JSON object",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials for login",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Login success",
                            responseCode = "200",
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
                            description = "Bad credentials",
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
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse loginUser(@RequestBody @Valid LoginDTO loginDTO,
                                  HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse){
        log.info("POST /api/auth/sign-in - Login user {}", loginDTO.username());
        return this.authService.loginUser(loginDTO, httpServletResponse, httpServletRequest);
    }

    @Operation(
            summary = "Logout method",
            description = "Logout method",
            responses = {
                    @ApiResponse(
                            description = "Logout success",
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

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        this.authService.logoutUser(httpServletRequest, httpServletResponse);
    }

}
