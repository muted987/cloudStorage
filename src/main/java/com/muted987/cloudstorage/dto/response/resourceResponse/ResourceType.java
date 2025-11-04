package com.muted987.cloudStorage.dto.response.resourceResponse;


public enum ResourceType {
    DIRECTORY("DIRECTORY"), FILE("FILE");

    private final String resourceTypeCation;

    ResourceType(String resourceTypeCaption) {
        this.resourceTypeCation = resourceTypeCaption;
    }

    String getResourceTypeCaption(){
        return resourceTypeCation;
    }



}
