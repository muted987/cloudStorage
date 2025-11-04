package com.muted987.cloudStorage.service.minioService;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ByteArrayMultipartFile implements MultipartFile {

    private final byte[] input;
    private final String name;
    private final String originalFileName;
    private final String contentType;

    public ByteArrayMultipartFile(byte[] input, String originalFileName) {
        this.contentType = "application/octet-stream";
        this.name = "file";
        this.input = input;
        this.originalFileName = originalFileName;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFileName;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public long getSize() {
        return input.length;
    }

    @Override
    public byte @NotNull [] getBytes() {
        return input;
    }

    @Override
    public @NotNull InputStream getInputStream() {
        return new ByteArrayInputStream(input);
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(input);
        }
    }
}
