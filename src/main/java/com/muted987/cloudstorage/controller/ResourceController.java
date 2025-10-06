package com.muted987.cloudStorage.controller;


import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.security.CustomUserDetails;
import com.muted987.cloudStorage.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final MinioService minioService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse uploadFile(@RequestPart("object") MultipartFile multipartFile,
                                   @RequestParam("path") String path,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return minioService.uploadFile(path, multipartFile, userDetails.getId());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@RequestParam("path") String path, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        minioService.deleteResource(path, userDetails.getId());
    }

    @GetMapping("move")
    public ResourceResponse moveFile(@RequestParam("from") String fromPath,
                                     @RequestParam("to") String toPath,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return minioService.moveResource(fromPath, toPath, userDetails.getId());
    }


}
