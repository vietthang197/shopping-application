package vn.com.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDto {
    private String id;
    private Set<OrderItemDto> orderItems = new LinkedHashSet<>();
    private CustomerDto customer;
    private LocalDateTime createdDt;
    private String status;
}
