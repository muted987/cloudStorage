package com.muted987.cloudStorage.controller.payload;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PathParam(
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^(?!.*/\\.(/|$))(?!(?:.*/)?[^/]*\\.[^/]*/)(?:[\\w\\s()-]+/)*[\\w\\s().-]*(?:\\.[\\w]+)?$", message = "Путь невалидный")
        String path
) {
}
