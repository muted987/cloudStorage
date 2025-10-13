package com.muted987.cloudStorage.exception;

import org.jetbrains.annotations.NotNull;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
