package com.muted987.cloudStorage.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record QueryParam(
        @NotNull(message="Отсутствует запрос")
        @Pattern(regexp="^/|(/[\\\\w-]+)+$", message = "Запрос")
        String query
) {
}
