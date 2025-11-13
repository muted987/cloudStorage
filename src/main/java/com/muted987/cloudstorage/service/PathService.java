package com.muted987.cloudStorage.service;

import com.muted987.cloudStorage.dto.response.ValidationResult;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Service
public class PathService {

    public ValidationResult validatePath(String path) {
        if (path == null) {
            return new ValidationResult(false, "Path cannot be null");
        }

        String trimmedPath = path.trim();

        if (trimmedPath.isEmpty()) {
            return new ValidationResult(true, "Path is valid");
        }

        if (containsPathTraversal(trimmedPath)) {
            return new ValidationResult(false, "Path traversal detected");
        }

        if (containsInvalidCharacters(trimmedPath)) {
            return new ValidationResult(false, "Path contains invalid characters");
        }

        if (!hasValidStructure(trimmedPath)) {
            return new ValidationResult(false, "Invalid path structure");
        }

        try {
            Paths.get(trimmedPath);
        } catch (InvalidPathException e) {
            return new ValidationResult(false, "Invalid path format: " + e.getMessage());
        }

        if (trimmedPath.length() > 4096) {
            return new ValidationResult(false, "Path is too long");
        }

        return new ValidationResult(true, "Path is valid");
    }

    private boolean containsPathTraversal(String path) {
        if (path.equals(".") || path.equals("..")) {
            return true;
        }

        if (path.startsWith("/") || path.startsWith("\\")) {
            return true;
        }

        if (Pattern.compile("^[A-Za-z]:[/\\\\]").matcher(path).find()) {
            return true;
        }

        if (path.contains("/../") || path.contains("\\..\\")) {
            return true;
        }

        if (path.startsWith("../") || path.startsWith("..\\")) {
            return true;
        }

        return path.endsWith("/..") || path.endsWith("\\..");
    }

    private boolean containsInvalidCharacters(String path) {
        String invalidChars = "[~<>:\"|?*\\x00-\\x1F]";
        return Pattern.compile(invalidChars).matcher(path).find();
    }

    private boolean hasValidStructure(String path) {
        if (path.contains("//") || path.contains("\\\\")) {
            return false;
        }

        String[] parts = path.split("[/\\\\]");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (part.isEmpty()) {
                continue;
            }

            if (!isValidPathPart(part, i == parts.length - 1, path.endsWith("/") || path.endsWith("\\"))) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidPathPart(String part, boolean isLastPart, boolean pathEndsWithSlash) {
        if (part.isEmpty()) {
            return false;
        }

        if (part.equals(".") || part.equals("..")) {
            return false;
        }

        if (isLastPart && !pathEndsWithSlash) {
            return isValidFileName(part);
        }

        return isValidFolderName(part);
    }

    private boolean isValidFileName(String name) {
        if (!name.contains(".")) {
            return false;
        }

        if (name.startsWith(".") || name.endsWith(".")) {
            return false;
        }

        return !name.contains("..");
    }

    private boolean isValidFolderName(String name) {
        if (name.contains(".")) {
            return false;
        }

        return !name.endsWith(" ") && !name.endsWith(".");
    }
}