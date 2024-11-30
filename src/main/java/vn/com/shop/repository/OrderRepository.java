package vn.com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByCustomer_AccountUsername(String username, Pageable pageRequest);
    Optional<Orders> findByIdAndCustomer_AccountUsername(String id, String username);
    Page<Orders> findByStatus(String status, Pageable pageRequest);

    @Query("SELECT SUM(oi.price * oi.quantity) FROM Orders o JOIN o.orderItems oi " +
            "WHERE o.createdDt BETWEEN :startDate AND :endDate AND o.status = :status")
    BigDecimal calculateRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate, String status);

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.createdDt BETWEEN :startDate AND :endDate")
    long countOrdersForPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.createdDt BETWEEN :startDate AND :endDate " +
            "AND o.status = :status")
    long countOrdersForPeriodAndStatus(LocalDateTime startDate, LocalDateTime endDate, String status);
}
