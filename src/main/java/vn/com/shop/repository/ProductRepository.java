package vn.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}