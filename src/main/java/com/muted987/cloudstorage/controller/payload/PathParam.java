package com.muted987.cloudStorage.controller.payload;


import com.muted987.cloudStorage.config.annotations.validation.ValidPath;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PathParam(

        @ValidPath
        @NotNull(message="Отсутствует путь")
        @Schema(description = "Path to directory", example = "folder1/file1.txt")
        String path

) {
}
