package com.muted987.cloudStorage.service;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.entity.User;
import com.muted987.cloudStorage.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UserMapper userMapper;
    private final ApplicationUserDetailsService applicationUserDetailsService;

    public UserResponse registerUser(RegisterDTO registerDTO,
                                     HttpServletResponse httpResponse,
                                     HttpServletRequest httpRequest){
        User user = this.userService.createUser(registerDTO);
        Authentication authenticationResponse = authenticateUser(registerDTO.username(), registerDTO.password());
        saveSecurityContext(httpResponse, httpRequest, authenticationResponse);
        return userMapper.toUserResponseFromUser(user);
    }

    public UserResponse loginUser(LoginDTO loginDTO,
                          HttpServletResponse httpResponse,
                          HttpServletRequest httpRequest) {
        Authentication authenticationResponse = authenticateUser(loginDTO.username(), loginDTO.password());
        saveSecurityContext(httpResponse, httpRequest, authenticationResponse);
        return userMapper.toUserResponseFromLoginDTO(loginDTO);
    }

    private void saveSecurityContext(HttpServletResponse httpResponse,
                                     HttpServletRequest httpRequest,
                                     Authentication authenticationResponse) {
        SecurityContext securityContext = new SecurityContextImpl(authenticationResponse);
        SecurityContextHolder.setContext(securityContext);
        this.securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);
    }

    private Authentication authenticateUser(String username, String password) {
        UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password);
        return this.authenticationManager.authenticate(authentication);
    }
}
