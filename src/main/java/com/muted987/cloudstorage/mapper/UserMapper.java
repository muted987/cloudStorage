package com.muted987.cloudStorage.mapper;


import com.muted987.cloudStorage.dto.request.LoginDTO;
import com.muted987.cloudStorage.dto.request.RegisterDTO;
import com.muted987.cloudStorage.dto.response.UserResponse;
import com.muted987.cloudStorage.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User fromRegisterDTOToUser(RegisterDTO registerDTO);
    UserResponse toUserResponseFromUser(User user);
    UserResponse toUserResponseFromLoginDTO(LoginDTO loginDTO);

}
