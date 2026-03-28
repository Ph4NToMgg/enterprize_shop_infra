package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderItemDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.entity.OrderItemEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-20T15:56:50+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toDto(OrderEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDto.OrderDtoBuilder orderDto = OrderDto.builder();

        orderDto.status( entity.getStatus() );
        orderDto.createdAt( entity.getCreatedAt() );
        orderDto.id( entity.getId() );
        orderDto.items( orderItemEntityListToOrderItemDtoList( entity.getItems() ) );
        orderDto.totalAmount( entity.getTotalAmount() );

        return orderDto.build();
    }

    @Override
    public OrderEntity toEntity(OrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderEntity.Builder orderEntity = OrderEntity.builder();

        orderEntity.id( dto.getId() );
        orderEntity.status( dto.getStatus() );
        orderEntity.totalAmount( dto.getTotalAmount() );
        orderEntity.createdAt( dto.getCreatedAt() );

        return orderEntity.build();
    }

    @Override
    public OrderItemDto toItemDto(OrderItemEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderItemDto.OrderItemDtoBuilder orderItemDto = OrderItemDto.builder();

        orderItemDto.id( entity.getId() );
        orderItemDto.quantity( entity.getQuantity() );
        orderItemDto.unitPrice( entity.getUnitPrice() );

        return orderItemDto.build();
    }

    @Override
    public List<OrderDto> toDtoList(List<OrderEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>( entities.size() );
        for ( OrderEntity orderEntity : entities ) {
            list.add( toDto( orderEntity ) );
        }

        return list;
    }

    @Override
    public OrderItemEntity toItemEntity(OrderItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderItemEntity.Builder orderItemEntity = OrderItemEntity.builder();

        orderItemEntity.id( dto.getId() );
        orderItemEntity.quantity( dto.getQuantity() );
        orderItemEntity.unitPrice( dto.getUnitPrice() );

        return orderItemEntity.build();
    }

    protected List<OrderItemDto> orderItemEntityListToOrderItemDtoList(List<OrderItemEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDto> list1 = new ArrayList<OrderItemDto>( list.size() );
        for ( OrderItemEntity orderItemEntity : list ) {
            list1.add( toItemDto( orderItemEntity ) );
        }

        return list1;
    }
}
