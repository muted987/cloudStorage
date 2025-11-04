package com.muted987.cloudStorage.mapper;


import com.muted987.cloudStorage.dto.response.resourceResponse.DirectoryResponse;
import com.muted987.cloudStorage.dto.response.resourceResponse.FileResponse;
import com.muted987.cloudStorage.utils.PathUtil;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {PathUtil.class})

public interface ResourceResponseMapper {

    /*
        В Path передавать FORMATTED_PATH ВСЕГДА
     */


    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, item.objectName()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, item.objectName()).get(\"path\"))")
    @Mapping(target = "size", expression = "java((int) item.size())")
    FileResponse toFileResponse(Item item, String path);

    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, item.objectName()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, item.objectName()).get(\"path\"))")
    DirectoryResponse toDirectoryResponse(Item item, String path);

    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, objectWriteResponse.object()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, objectWriteResponse.object()).get(\"path\"))")
    DirectoryResponse toDirectoryResponse(ObjectWriteResponse objectWriteResponse, String path);

    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, objectWriteResponse.object()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, objectWriteResponse.object()).get(\"path\"))")
    FileResponse toFileResponse(ObjectWriteResponse objectWriteResponse, String path);

    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, statObjectResponse.object()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, statObjectResponse.object()).get(\"path\"))")
    DirectoryResponse toDirectoryResponse(StatObjectResponse statObjectResponse, String path);

    @Mapping(target = "name", expression = "java(PathUtil.extractData(path, statObjectResponse.object()).get(\"name\"))")
    @Mapping(target = "path", expression = "java(PathUtil.extractData(path, statObjectResponse.object()).get(\"path\"))")
    @Mapping(target = "size", expression = "java((int) statObjectResponse.size())")
    FileResponse toFileResponse(StatObjectResponse statObjectResponse, String path);

}
