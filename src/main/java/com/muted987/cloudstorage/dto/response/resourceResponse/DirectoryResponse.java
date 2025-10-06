package com.muted987.cloudStorage.dto.response.resourceResponse;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class DirectoryResponse extends ResourceResponse {

    private final ResourceType type = ResourceType.DIRECTORY;



}
