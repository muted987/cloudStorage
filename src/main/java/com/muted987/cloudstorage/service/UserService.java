package com.muted987.cloudStorage.service;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.entity.User;
import com.muted987.cloudStorage.exception.UserAlreadyExistException;
import com.muted987.cloudStorage.mapper.UserMapper;
import com.muted987.cloudStorage.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public User createUser(RegisterDTO registerDTO) {
        try {
            User newUser = this.userMapper.fromRegisterDTOToUser(registerDTO);
            newUser.setPassword(this.bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setRole("USER");
            log.debug("Creating new user with {} username", registerDTO.username());
            return this.userRepository.save(newUser);
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException("User with name %s already exist".formatted(registerDTO.username()));
        }
    }

    public Optional<User> getUser(LoginDTO loginDTO) {
        log.debug("Requesting user with {} username", loginDTO.username());
        return this.userRepository.findByUsername(loginDTO.username());
    }

}
