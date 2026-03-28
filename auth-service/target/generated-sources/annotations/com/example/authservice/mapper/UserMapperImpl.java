package com.example.authservice.mapper;

import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-20T15:56:44+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto.Builder userDto = UserDto.builder();

        userDto.id( entity.getId() );
        userDto.username( entity.getUsername() );
        userDto.email( entity.getEmail() );
        userDto.role( entity.getRole() );
        userDto.enabled( entity.isEnabled() );

        return userDto.build();
    }

    @Override
    public List<UserDto> toDtoList(List<UserEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( entities.size() );
        for ( UserEntity userEntity : entities ) {
            list.add( toDto( userEntity ) );
        }

        return list;
    }
}
