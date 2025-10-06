package com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory;

import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;

public class FileResponseFactory extends ResourceResponseFactory<FileResponse> {

    public FileResponse createResourceResponse(String path, String name, int size) {
        return FileResponse.builder()
                .path(path)
                .name(name)
                .size(size)
                .build();
    }


    @Override
    public FileResponse createResourceResponse(String path, String name) {
        return createResourceResponse(path, name, 0);
    }
}
