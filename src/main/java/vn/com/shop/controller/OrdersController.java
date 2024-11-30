package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.entity.Orders;
import vn.com.shop.mapper.OrdersMapper;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductCategoryService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable String orderId, Model model) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);
        Orders order = orderService.getCurrentUserOrders(orderId)
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

    @GetMapping("/tracking")
    public String viewMyOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<OrdersDto> orders = orderService.getCurrentUserOrders(
                username,
                PageRequest.of(page, size, Sort.by("createdDt").descending())
        );

        model.addAttribute("orders", orders);
        return "order/tracking";
    }
}
