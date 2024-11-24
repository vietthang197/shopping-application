package vn.com.shop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    List<ProductCategory> getAllCategories();
    Product getProductById(String id);
    Product createProduct(Product product);
    Product updateProduct(String id, Product product);
    void deleteProduct(String id);

    List<Product> findPopularProduct();
    Optional<Product> findById(String id);
}
