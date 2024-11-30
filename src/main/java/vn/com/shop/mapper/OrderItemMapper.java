package vn.com.shop.mapper;

import vn.com.shop.dto.OrderItemDto;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.entity.OrderItem;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderItemMapper {
    public static OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .product(ProductMapper.toDto(orderItem.getProduct()))
                .build();
    }

    public static Set<OrderItemDto> toSetDto(Set<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.stream().map(OrderItemMapper::toDto).collect(Collectors.toSet());
    }
}
