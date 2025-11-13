package com.muted987.cloudStorage.service;

import com.muted987.cloudStorage.dto.response.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PathService.class)
class PathServiceTest {

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "folder1/folder2/file.txt",
            "folder1/folder2/file (1).txt",
            "file.txt",
            "document.pdf",
            "image.jpg",
            "data.json",
            "archive.tar.gz",
            "folder/",
            "folder1/folder2/",
            "my folder/my file.txt"
    })
    @DisplayName("Should accept valid paths")
    void validatePath_ValidPaths_ReturnsValid(String path) {
        ValidationResult result = pathService.validatePath(path);

        assertTrue(result.valid(), "Path should be valid: " + path + " - " + result.message());
        assertEquals("Path is valid", result.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "folder",
            ".hidden",
            "file.",
            "file..txt",
            "folder/../file.txt",
            "../file.txt",
            "/absolute/path",
            "C:\\Windows",
            "file<.txt",
            "file|name.txt"
    })
    @DisplayName("Should reject invalid paths")
    void validatePath_InvalidPaths_ReturnsInvalid(String path) {
        ValidationResult result = pathService.validatePath(path);

        assertFalse(result.valid(), "Path should be invalid: " + path);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "folder1/folder2/file.name.txt",
            "file (1).txt",
            "file-name.txt",
            "file_name.txt",
            "123.txt",
            "folder123/file456.txt"
    })
    @DisplayName("Should accept edge cases that are valid")
    void validatePath_ValidEdgeCases_ReturnsValid(String path) {
        ValidationResult result = pathService.validatePath(path);

        assertTrue(result.valid(), "Path should be valid: " + path + " - " + result.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "..txt",
            "folder.../file.txt",
            "~home/file.txt",
            "folder/./file.txt",
            "folder//file.txt"
    })
    @DisplayName("Should reject edge cases that are invalid")
    void validatePath_InvalidEdgeCases_ReturnsInvalid(String path) {
        ValidationResult result = pathService.validatePath(path);

        assertFalse(result.valid(), "Path should be invalid: " + path + " - " + result.message());
    }

    @Test
    @DisplayName("Should return valid for empty string")
    void validatePath_EmptyString_ReturnsValid() {
        ValidationResult result = pathService.validatePath("");

        assertTrue(result.valid());
        assertEquals("Path is valid", result.message());
    }

    @Test
    @DisplayName("Should return invalid for null")
    void validatePath_Null_ReturnsInvalid() {
        ValidationResult result = pathService.validatePath(null);

        assertFalse(result.valid());
        assertEquals("Path cannot be null", result.message());
    }
}