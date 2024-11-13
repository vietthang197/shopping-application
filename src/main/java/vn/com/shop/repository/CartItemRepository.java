package vn.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
}