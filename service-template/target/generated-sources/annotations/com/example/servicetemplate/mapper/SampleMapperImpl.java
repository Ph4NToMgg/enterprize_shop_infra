package com.example.servicetemplate.mapper;

import com.example.servicetemplate.dto.SampleDto;
import com.example.servicetemplate.entity.SampleEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-20T15:56:52+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class SampleMapperImpl implements SampleMapper {

    @Override
    public SampleDto toDto(SampleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SampleDto.SampleDtoBuilder sampleDto = SampleDto.builder();

        sampleDto.createdAt( entity.getCreatedAt() );
        sampleDto.description( entity.getDescription() );
        sampleDto.id( entity.getId() );
        sampleDto.name( entity.getName() );
        sampleDto.updatedAt( entity.getUpdatedAt() );

        return sampleDto.build();
    }

    @Override
    public SampleEntity toEntity(SampleDto dto) {
        if ( dto == null ) {
            return null;
        }

        SampleEntity.SampleEntityBuilder sampleEntity = SampleEntity.builder();

        sampleEntity.createdAt( dto.getCreatedAt() );
        sampleEntity.description( dto.getDescription() );
        sampleEntity.id( dto.getId() );
        sampleEntity.name( dto.getName() );
        sampleEntity.updatedAt( dto.getUpdatedAt() );

        return sampleEntity.build();
    }

    @Override
    public List<SampleDto> toDtoList(List<SampleEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SampleDto> list = new ArrayList<SampleDto>( entities.size() );
        for ( SampleEntity sampleEntity : entities ) {
            list.add( toDto( sampleEntity ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(SampleDto dto, SampleEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setDescription( dto.getDescription() );
        entity.setId( dto.getId() );
        entity.setName( dto.getName() );
        entity.setUpdatedAt( dto.getUpdatedAt() );
    }
}
