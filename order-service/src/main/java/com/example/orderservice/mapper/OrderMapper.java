package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderItemDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "status", source = "status")
    OrderDto toDto(OrderEntity entity);

    @Mapping(target = "items", ignore = true)
    OrderEntity toEntity(OrderDto dto);

    OrderItemDto toItemDto(OrderItemEntity entity);
    List<OrderDto> toDtoList(List<OrderEntity> entities);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toItemEntity(OrderItemDto dto);
}
