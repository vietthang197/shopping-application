package vn.com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Orders;

import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByCustomer_AccountUsername(String username, Pageable pageRequest);
    Optional<Orders> findByIdAndCustomer_AccountUsername(String id, String username);
}
