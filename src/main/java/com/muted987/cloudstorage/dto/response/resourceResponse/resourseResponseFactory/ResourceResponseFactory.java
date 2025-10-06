package com.muted987.cloudStorage.dto.response.resourceResponse.resourseResponseFactory;

import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;

public abstract class ResourceResponseFactory<T extends ResourceResponse> {

    public abstract T createResourceResponse(String path, String name);

}
