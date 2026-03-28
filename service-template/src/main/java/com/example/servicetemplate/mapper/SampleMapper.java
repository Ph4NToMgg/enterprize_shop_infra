package com.example.servicetemplate.mapper;

import com.example.servicetemplate.dto.SampleDto;
import com.example.servicetemplate.entity.SampleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleMapper {

    SampleDto toDto(SampleEntity entity);

    SampleEntity toEntity(SampleDto dto);

    List<SampleDto> toDtoList(List<SampleEntity> entities);

    void updateEntityFromDto(SampleDto dto, @MappingTarget SampleEntity entity);
}
