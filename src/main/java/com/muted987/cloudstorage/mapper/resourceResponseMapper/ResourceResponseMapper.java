package com.muted987.cloudStorage.mapper.resourceResponseMapper;

import com.muted987.cloudStorage.dto.response.resourceResponse.ResourceResponse;
import com.muted987.cloudStorage.service.minioService.ItemInfo;

import java.util.List;

public interface ResourceResponseMapper {

    ResourceResponse toResourceResponse(ItemInfo itemInfo);

    List<ResourceResponse> toResourceResponseList(List<ItemInfo> itemInfoList);


}
