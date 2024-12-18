package vn.com.shop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import vn.com.shop.dto.OrderStatistics;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Orders;
import vn.com.shop.request.CheckoutRequest;

import java.util.Optional;

public interface OrderService {
    Orders createOrder(CheckoutRequest request, ShoppingCart cart, String username);
    Optional<Orders> getOrderById(String orderId);
    String getOrderStatusText(String status);

    Page<OrdersDto> getCurrentUserOrders(String username, PageRequest createdDt);
    Optional<Orders> getCurrentUserOrders(String orderId);
    Page<OrdersDto> getAllOrders(String status, Pageable pageable);
    Orders updateOrderStatus(String orderId, String status);
    boolean isValidStatusTransition(String currentStatus, String newStatus);
    OrderStatistics getStatistics(int year, String period);
}
