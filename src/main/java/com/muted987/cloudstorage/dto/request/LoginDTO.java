package com.muted987.cloudStorage.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @Size(min=5, max=15, message="Длина логина должна быть между 5 и 15 символами")
        @NotNull(message="Заполните поле Логин")
        String username,
        @Size(min=8, message="Длина пароля должна быть более 8 символов")
        @NotNull(message="Заполните поле Пароль")
        String password
){ }
