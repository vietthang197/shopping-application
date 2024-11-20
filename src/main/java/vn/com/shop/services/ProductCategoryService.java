package vn.com.shop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.shop.entity.ProductCategory;

public interface ProductCategoryService {
    Page<ProductCategory> getAllProductCategories(Pageable pageable);

    ProductCategory getProductCategoryById(String id);

    ProductCategory createProductCategory(ProductCategory productCategory);

    ProductCategory updateProductCategory(String id, ProductCategory productCategory);

    void deleteProductCategory(String id);
}
