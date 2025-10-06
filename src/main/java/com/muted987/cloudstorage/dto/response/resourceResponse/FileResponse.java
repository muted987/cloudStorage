package com.muted987.cloudStorage.dto.response.resourceResponse;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FileResponse extends ResourceResponse {

    private int size;
    private final ResourceType type = ResourceType.FILE;

}
