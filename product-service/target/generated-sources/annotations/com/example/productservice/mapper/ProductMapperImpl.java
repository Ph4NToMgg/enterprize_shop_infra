package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-20T15:56:51+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.active( entity.isActive() );
        productDto.category( entity.getCategory() );
        productDto.createdAt( entity.getCreatedAt() );
        productDto.description( entity.getDescription() );
        productDto.id( entity.getId() );
        productDto.imageUrl( entity.getImageUrl() );
        productDto.name( entity.getName() );
        productDto.price( entity.getPrice() );
        productDto.sku( entity.getSku() );
        productDto.stockHint( entity.getStockHint() );
        productDto.updatedAt( entity.getUpdatedAt() );

        return productDto.build();
    }

    @Override
    public ProductEntity toEntity(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductEntity.ProductEntityBuilder productEntity = ProductEntity.builder();

        productEntity.active( dto.isActive() );
        productEntity.category( dto.getCategory() );
        productEntity.createdAt( dto.getCreatedAt() );
        productEntity.description( dto.getDescription() );
        productEntity.id( dto.getId() );
        productEntity.imageUrl( dto.getImageUrl() );
        productEntity.name( dto.getName() );
        productEntity.price( dto.getPrice() );
        productEntity.sku( dto.getSku() );
        productEntity.stockHint( dto.getStockHint() );
        productEntity.updatedAt( dto.getUpdatedAt() );

        return productEntity.build();
    }

    @Override
    public List<ProductDto> toDtoList(List<ProductEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( entities.size() );
        for ( ProductEntity productEntity : entities ) {
            list.add( toDto( productEntity ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(ProductDto dto, ProductEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActive( dto.isActive() );
        entity.setCategory( dto.getCategory() );
        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setDescription( dto.getDescription() );
        entity.setId( dto.getId() );
        entity.setImageUrl( dto.getImageUrl() );
        entity.setName( dto.getName() );
        entity.setPrice( dto.getPrice() );
        entity.setSku( dto.getSku() );
        entity.setStockHint( dto.getStockHint() );
        entity.setUpdatedAt( dto.getUpdatedAt() );
    }
}
