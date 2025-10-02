package com.muted987.cloudStorage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record ExceptionMessage(
        String message
) {
}
