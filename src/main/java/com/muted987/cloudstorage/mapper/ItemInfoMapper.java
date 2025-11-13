package com.muted987.cloudStorage.mapper;

import com.muted987.cloudStorage.service.minioService.ItemInfo;
import com.muted987.cloudStorage.utils.PathUtil;
import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemInfoMapper {

    @Mapping(target = "path", expression = "java(extractData(statObjectResponse.object()).get(\"path\"))")
    @Mapping(target = "name", expression = "java(extractData(statObjectResponse.object()).get(\"name\"))")
    @Mapping(target = "size", expression = "java((int) statObjectResponse.size())")
    ItemInfo toItemInfo(StatObjectResponse statObjectResponse);

    @Mapping(target = "path", expression = "java(extractData(objectWriteResponse.object()).get(\"path\"))")
    @Mapping(target = "name", expression = "java(extractData(objectWriteResponse.object()).get(\"name\"))")
    @Mapping(target = "size", ignore = true)
    ItemInfo toItemInfo(ObjectWriteResponse objectWriteResponse);

    @Mapping(target = "path", expression = "java(extractData(item.objectName()).get(\"path\"))")
    @Mapping(target = "name", expression = "java(extractData(item.objectName()).get(\"name\"))")
    @Mapping(target = "size", expression = "java((int) item.size())")
    ItemInfo toItemInfo(Item item);

    default List<ItemInfo> toItemInfoList(Iterable<Result<Item>> results, String requestPath) {
        List<ItemInfo> itemInfoList = new ArrayList<>();
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                ItemInfo itemInfo = toItemInfo(item);
                if (itemInfo.name().isEmpty() || isRequestedFolderItself(item, requestPath)) {
                    continue;
                }
                itemInfoList.add(itemInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return itemInfoList;
    }

    @Named("extractData")
    default Map<String, String> extractData(String path) {
        Map<String, String> data = new HashMap<>();
        String reformatPath = PathUtil.reformatPath(path);
        String parentPath = PathUtil.getParentPath(reformatPath);

        String name;
        if (parentPath.isEmpty() || parentPath.equals(reformatPath)) {
            name = reformatPath;
        } else {
            name = reformatPath.substring(parentPath.length());
        }

        data.put("path", parentPath);
        data.put("name", name);
        return data;
    }

    default boolean isRequestedFolderItself(Item item, String requestedPath) {
        return item.objectName().equals(requestedPath);
    }
}