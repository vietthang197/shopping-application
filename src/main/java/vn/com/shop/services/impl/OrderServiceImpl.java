package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Customer;
import vn.com.shop.entity.OrderItem;
import vn.com.shop.entity.Orders;
import vn.com.shop.repository.CustomerRepository;
import vn.com.shop.repository.OrderRepository;
import vn.com.shop.request.CheckoutRequest;
import vn.com.shop.services.AccountService;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(CheckoutRequest request, ShoppingCart cart, String username) {
        // Tạo hoặc cập nhật customer
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCreatedDt(LocalDateTime.now());
        Account account = accountService.findAccountByUsername(username);
        customer.setAccount(account);

        // Tạo order
        Orders order = new Orders();
        order.setCustomer(customer);
        order.setCreatedDt(LocalDateTime.now());
        order.setStatus("PENDING"); // Trạng thái chờ xử lý

        // Tạo order items
        Set<OrderItem> orderItems = cart.getItems().values().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrders(order);
                    orderItem.setProduct(productService.findById(cartItem.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found")));
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Orders> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public String getOrderStatusText(String status) {
        return switch (status) {
            case "PENDING" -> "Đang chờ xử lý";
            case "CONFIRMED" -> "Đã xác nhận";
            case "SHIPPING" -> "Đang giao hàng";
            case "COMPLETED" -> "Đã giao hàng";
            case "CANCELLED" -> "Đã hủy";
            default -> status;
        };
    }

}
