package com.muted987.cloudStorage.controller.payload;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PathParam(
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^([a-zA-Z0-9_-]+\\/)*[a-zA-Z0-9_.-]*$", message = "Путь невалидный")
        String path
) {
}
