package com.example.authservice.mapper;

import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.UserEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity entity);
    List<UserDto> toDtoList(List<UserEntity> entities);
}
