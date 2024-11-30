package vn.com.shop.mapper;

import vn.com.shop.dto.OrdersDto;
import vn.com.shop.entity.Orders;

public class OrdersMapper {
    public static OrdersDto toDto(Orders orders) {
        return OrdersDto.builder()
                .id(orders.getId())
                .createdDt(orders.getCreatedDt())
                .status(orders.getStatus())
                .orderItems(OrderItemMapper.toSetDto(orders.getOrderItems()))
                .customer(CustomerMapper.toDto(orders.getCustomer()))
                .customerNote(orders.getCustomerNote())
                .build();
    }
}
