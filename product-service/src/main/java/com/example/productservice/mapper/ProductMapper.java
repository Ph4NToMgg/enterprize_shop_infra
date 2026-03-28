package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(ProductEntity entity);
    ProductEntity toEntity(ProductDto dto);
    List<ProductDto> toDtoList(List<ProductEntity> entities);
    void updateEntityFromDto(ProductDto dto, @MappingTarget ProductEntity entity);
}
