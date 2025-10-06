package com.muted987.cloudStorage.dto.response.resourceResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ResourceResponse {
    protected String path;
    protected String name;

}
