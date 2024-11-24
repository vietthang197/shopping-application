package vn.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Orders;

@Repository
public interface CartRepository extends JpaRepository<Orders, String> {
}