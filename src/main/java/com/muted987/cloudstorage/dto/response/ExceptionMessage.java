package com.muted987.cloudStorage.dto.response;

import lombok.Builder;

@Builder
public record ExceptionMessage(
        String message
) {
}
