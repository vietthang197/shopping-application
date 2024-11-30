package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.shop.dto.OrderStatistics;
import vn.com.shop.dto.OrdersDto;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Customer;
import vn.com.shop.entity.OrderItem;
import vn.com.shop.entity.Orders;
import vn.com.shop.mapper.OrdersMapper;
import vn.com.shop.repository.CustomerRepository;
import vn.com.shop.repository.OrderRepository;
import vn.com.shop.request.CheckoutRequest;
import vn.com.shop.services.AccountService;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductService;
import vn.com.shop.util.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        order.setStatus(OrderStatus.PENDING.name()); // Trạng thái chờ xử lý
        order.setCustomerNote(request.getNote());

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

    @Override
    public Page<OrdersDto> getCurrentUserOrders(String username, PageRequest pageRequest) {
        return orderRepository.findByCustomer_AccountUsername(username, pageRequest)
                .map(orders -> {
                    OrdersDto ordersDto = OrdersMapper.toDto(orders);
                    BigDecimal totalAmount = BigDecimal.ZERO;
                    for (OrderItem orderItem : orders.getOrderItems()) {
                        totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                    }
                    ordersDto.setTotalAmount(totalAmount);
                    return ordersDto;
                });
    }

    @Override
    public Optional<Orders> getCurrentUserOrders(String orderId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepository.findByIdAndCustomer_AccountUsername(orderId, username);
    }

    @Override
    public Page<OrdersDto> getAllOrders(String status, Pageable pageable) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findByStatus(status, pageable).map(orders -> {
                OrdersDto ordersDto = OrdersMapper.toDto(orders);
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (OrderItem orderItem : orders.getOrderItems()) {
                    totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                }
                ordersDto.setTotalAmount(totalAmount);
                return ordersDto;
            });
        }
        return orderRepository.findAll(pageable).map(orders -> {
            OrdersDto ordersDto = OrdersMapper.toDto(orders);
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItem orderItem : orders.getOrderItems()) {
                totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            }
            ordersDto.setTotalAmount(totalAmount);
            return ordersDto;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders updateOrderStatus(String orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), status)) {
            throw new RuntimeException("Không thể chuyển từ trạng thái " +
                    order.getStatus() + " sang " + status);
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // Add your status transition rules here
        if (currentStatus.equals("CANCELLED")) {
            return false; // Cannot change status of cancelled order
        }
        if (currentStatus.equals("COMPLETED")) {
            return false; // Cannot change status of completed order
        }
        return true;
    }

    @Override
    public OrderStatistics getStatistics(int year, String period) {
        OrderStatistics stats = new OrderStatistics();

        if ("month".equals(period)) {
            // Get monthly statistics for selected year
            List<OrderStatistics.MonthlyStats> monthlyStats = new ArrayList<>();

            for (int month = 1; month <= 12; month++) {
                LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

                OrderStatistics.MonthlyStats monthStat = new OrderStatistics.MonthlyStats();
                monthStat.setMonth(month);
                monthStat.setYear(year);

                // Get revenue for completed orders
                BigDecimal revenue = orderRepository.calculateRevenueForPeriod(
                        startDate, endDate, "COMPLETED");
                monthStat.setRevenue(revenue != null ? revenue : BigDecimal.ZERO);

                // Get order counts
                monthStat.setTotalOrders(orderRepository.countOrdersForPeriod(startDate, endDate));
                monthStat.setCompletedOrders(orderRepository.countOrdersForPeriodAndStatus(
                        startDate, endDate, "COMPLETED"));
                monthStat.setCancelledOrders(orderRepository.countOrdersForPeriodAndStatus(
                        startDate, endDate, "CANCELLED"));

                monthlyStats.add(monthStat);
            }

            stats.setMonthlyStats(monthlyStats);

        } else {
            // Get yearly statistics for last 5 years
            List<OrderStatistics.YearlyStats> yearlyStats = new ArrayList<>();
            int currentYear = LocalDateTime.now().getYear();

            for (int i = 4; i >= 0; i--) {
                int statsYear = currentYear - i;
                LocalDateTime startDate = LocalDateTime.of(statsYear, 1, 1, 0, 0);
                LocalDateTime endDate = startDate.plusYears(1).minusSeconds(1);

                OrderStatistics.YearlyStats yearStat = new OrderStatistics.YearlyStats();
                yearStat.setYear(statsYear);

                BigDecimal revenue = orderRepository.calculateRevenueForPeriod(
                        startDate, endDate, "COMPLETED");
                yearStat.setRevenue(revenue != null ? revenue : BigDecimal.ZERO);

                yearStat.setTotalOrders(orderRepository.countOrdersForPeriod(startDate, endDate));
                yearStat.setCompletedOrders(orderRepository.countOrdersForPeriodAndStatus(
                        startDate, endDate, "COMPLETED"));
                yearStat.setCancelledOrders(orderRepository.countOrdersForPeriodAndStatus(
                        startDate, endDate, "CANCELLED"));

                yearlyStats.add(yearStat);
            }

            stats.setYearlyStats(yearlyStats);
        }

        return stats;
    }
}
