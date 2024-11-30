package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.com.shop.dto.OrderItemDto;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.entity.OrderItem;
import vn.com.shop.entity.Orders;
import vn.com.shop.mapper.OrdersMapper;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductCategoryService;
import vn.com.shop.util.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/orders/{orderId}")
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

    @GetMapping("/orders/tracking")
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

    @GetMapping("/admin/orders")
    public String listOrders(@RequestParam(required = false) String status,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {

        Page<OrdersDto> orders = orderService.getAllOrders(status,
                PageRequest.of(page, size, Sort.by("createdDt").descending()));

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("currentStatus", status);

        return "order/admin_order_list";
    }

    @PostMapping("/admin/orders/{orderId}/status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@PathVariable String orderId,
                                          @RequestParam String status) {
        try {
            Orders updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cập nhật trạng thái thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/admin/orders/{orderId}/detail")
    public String viewOrderDetail(@PathVariable String orderId, Model model) {
        Orders order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng không tồn tại"));

        OrdersDto ordersDto = OrdersMapper.toDto(order);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDto orderItem : ordersDto.getOrderItems()) {
            totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        ordersDto.setTotalAmount(totalAmount);

        model.addAttribute("order", ordersDto);
        model.addAttribute("statuses", OrderStatus.values());

        return "order/admin_order_detail";
    }
}
