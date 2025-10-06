package com.muted987.cloudStorage.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value="id")
public record UserResponse(
        int id,
        String username
) {
}
