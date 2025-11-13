package com.muted987.cloudStorage.controller.payload;

import com.muted987.cloudStorage.config.annotations.validation.ValidPath;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MoveParam(
        @ValidPath
        @NotNull(message="Отсутствует путь")
        @Schema(description = "Path from", example = "folder1/file1.txt")
        String from,
        @ValidPath
        @NotNull(message="Отсутствует путь")
        @Schema(description = "Path to", example = "folder2/file1.txt")
        String to
) {
}