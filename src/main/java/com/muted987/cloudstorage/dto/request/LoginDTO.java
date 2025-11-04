package com.muted987.cloudStorage.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @Size(min=5, max=20, message="Длина логина должна быть между {min} и {max} символами")
        @NotNull(message="Заполните поле Логин")
        String username,
        @Size(min=5, max=20, message="Длина пароля должна быть между {min} и {max} символами")
        @NotNull(message="Заполните поле Пароль")
        String password
){ }
