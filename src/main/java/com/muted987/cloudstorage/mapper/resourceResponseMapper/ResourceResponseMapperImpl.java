package com.muted987.cloudStorage.mapper.resourceResponseMapper;

import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.service.minioService.ItemInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceResponseMapperImpl implements ResourceResponseMapper {

    @Override
    public ResourceResponse toResourceResponse(ItemInfo itemInfo) {
        if (itemInfo.size() == 0){
            return DirectoryResponse.builder()
                    .path(itemInfo.path())
                    .name(itemInfo.name())
                    .build();
        } else {
            return FileResponse.builder()
                    .path(itemInfo.path())
                    .name(itemInfo.name())
                    .size(itemInfo.size())
                    .build();
        }
    }

    @Override
    public List<ResourceResponse> toResourceResponseList(List<ItemInfo> itemInfoList) {
        List<ResourceResponse> resourceResponses = new ArrayList<>();
        for (ItemInfo itemInfo : itemInfoList){
            resourceResponses.add(toResourceResponse(itemInfo));
        }
        return resourceResponses;
    }
}
