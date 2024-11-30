package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.entity.Orders;
import vn.com.shop.mapper.OrdersMapper;
import vn.com.shop.services.OrderService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable String orderId, Model model) {
        Orders order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));

        OrdersDto ordersDto = OrdersMapper.toDto(order);

        model.addAttribute("order", ordersDto);
        model.addAttribute("statusText", orderService.getOrderStatusText(order.getStatus()));

        BigDecimal totalAmount = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalAmount", totalAmount);

        return "order/detail";
    }
}
