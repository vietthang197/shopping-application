package vn.com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);
    boolean existsBySkuAndIdNot(String sku, String id);
    Page<Product> findByProductCategory_Id(String productCategoryId, PageRequest pageRequest);
}