package vn.com.shop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    Page<ProductCategory> getAllProductCategories(Pageable pageable);

    List<ProductCategory> getAllProductCategories();

    ProductCategory getProductCategoryById(String id);

    ProductCategory createProductCategory(ProductCategory productCategory);

    ProductCategory updateProductCategory(String id, ProductCategory productCategory);

    void deleteProductCategory(String id);

    List<ProductCategoryDto> findAll();
    Optional<ProductCategory> findById(String id);
}
