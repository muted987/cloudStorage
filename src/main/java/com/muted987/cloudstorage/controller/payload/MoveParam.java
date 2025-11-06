package com.muted987.cloudStorage.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MoveParam(
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^(?!.*/\\.(/|$))(?!(?:.*/)?[^/]*\\.[^/]*/)(?:[\\w\\s()-]+/)*[\\w\\s().-]*(?:\\.[\\w]+)?$", message = "Путь невалидный")
        @Schema(description = "Path from", example = "folder1/file1.txt")
        String from,
        @NotNull(message="Отсутствует путь")
        @Pattern(regexp="^(?!.*/\\.(/|$))(?!(?:.*/)?[^/]*\\.[^/]*/)(?:[\\w\\s()-]+/)*[\\w\\s().-]*(?:\\.[\\w]+)?$", message = "Путь невалидный")
        @Schema(description = "Path to", example = "folder2/file1.txt")
        String to
) {
}
