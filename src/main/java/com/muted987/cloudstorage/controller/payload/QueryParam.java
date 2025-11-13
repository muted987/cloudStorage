package com.muted987.cloudStorage.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record QueryParam(
        @NotNull(message="Отсутствует запрос")
        @Schema(description = "Query", example = "file1.txt")
        String query
) {
}
