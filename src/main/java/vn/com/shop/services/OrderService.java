package vn.com.shop.services;

import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Orders;
import vn.com.shop.request.CheckoutRequest;

import java.util.Optional;

public interface OrderService {
    Orders createOrder(CheckoutRequest request, ShoppingCart cart, String username);
    Optional<Orders> getOrderById(String orderId);
    String getOrderStatusText(String status);
}
