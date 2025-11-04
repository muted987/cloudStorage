package com.muted987.cloudStorage.service;


import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.entity.User;
import com.muted987.cloudStorage.exception.UserAlreadyExistException;
import com.muted987.cloudStorage.mapper.UserMapper;
import com.muted987.cloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            newUser.setRole("ROLE_USER");
            log.debug("Creating new user with {} username", registerDTO.username());
            return this.userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException constraintViolationException) {
                if (Objects.requireNonNull(constraintViolationException.getMessage()).contains("unique")) {
                    throw new UserAlreadyExistException("User with name %s already exist".formatted(registerDTO.username()));
                }
            }
            throw new RuntimeException();
        } catch (RuntimeException e){
            throw new RuntimeException();
        }
    }

}
