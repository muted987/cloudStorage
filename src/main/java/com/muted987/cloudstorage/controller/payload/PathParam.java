package com.muted987.cloudStorage.controller.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PathParam(
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^(?!.*/\\.(/|$))(?!(?:.*/)?[^/]*\\.[^/]*/)(?:[\\w\\s()-]+/)*[\\w\\s().-]*(?:\\.[\\w]+)?$", message = "Путь невалидный")
        @Schema(description = "Path to directory", example = "folder1/file1.txt")
        String path
) {
}
