package com.muted987.cloudStorage.dto.response.resourceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FileResponse extends ResourceResponse {

    private int size;
    @Builder.Default
    private ResourceType type = ResourceType.FILE;

}
