package com.muted987.cloudStorage.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MoveParam(
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^/|(/[\\\\w-]+)+$", message = "Путь невалидный")
        String from,
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^/|(/[\\\\w-]+)+$", message = "Путь невалидный")
        String to
) {
}
