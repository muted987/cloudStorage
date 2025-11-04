package com.muted987.cloudStorage.dto.response.resourceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class DirectoryResponse extends ResourceResponse {

    @Builder.Default
    private ResourceType type = ResourceType.DIRECTORY;



}
