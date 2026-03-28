package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.entity.InventoryEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryDto toDto(InventoryEntity entity);
    InventoryEntity toEntity(InventoryDto dto);
    List<InventoryDto> toDtoList(List<InventoryEntity> entities);
}
