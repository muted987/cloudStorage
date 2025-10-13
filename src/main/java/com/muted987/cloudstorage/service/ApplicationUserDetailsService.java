package com.muted987.cloudStorage.service;


import com.muted987.cloudStorage.repository.UserRepository;
import com.muted987.cloudStorage.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> CustomUserDetails.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .build()).orElseThrow(() -> new UsernameNotFoundException("User with %s name not found".formatted(username)));
    }
}
