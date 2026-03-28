package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.entity.InventoryEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-20T15:56:49+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public InventoryDto toDto(InventoryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InventoryDto.Builder inventoryDto = InventoryDto.builder();

        inventoryDto.sku( entity.getSku() );

        return inventoryDto.build();
    }

    @Override
    public InventoryEntity toEntity(InventoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        InventoryEntity inventoryEntity = new InventoryEntity();

        inventoryEntity.setSku( dto.getSku() );

        return inventoryEntity;
    }

    @Override
    public List<InventoryDto> toDtoList(List<InventoryEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<InventoryDto> list = new ArrayList<InventoryDto>( entities.size() );
        for ( InventoryEntity inventoryEntity : entities ) {
            list.add( toDto( inventoryEntity ) );
        }

        return list;
    }
}
