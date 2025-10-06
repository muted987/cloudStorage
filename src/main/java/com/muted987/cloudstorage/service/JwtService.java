package com.muted987.cloudStorage.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

}
