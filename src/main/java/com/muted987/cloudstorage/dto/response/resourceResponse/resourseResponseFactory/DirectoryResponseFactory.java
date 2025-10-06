package com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;

public class DirectoryResponseFactory extends ResourceResponseFactory<DirectoryResponse> {

    @Override
    public DirectoryResponse createResourceResponse(String path, String name) {
        return DirectoryResponse.builder()
                .name(name)
                .path(path)
                .build();
    }

}
