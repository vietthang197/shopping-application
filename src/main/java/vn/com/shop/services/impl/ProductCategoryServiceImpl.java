package vn.com.shop.services.impl;

import org.springframework.stereotype.Service;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.entity.ProductCategory;
import vn.com.shop.repository.ProductCategoryRepository;
import vn.com.shop.services.ProductCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public Page<ProductCategory> getAllProductCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory getProductCategoryById(String id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Category not found"));
    }

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        if (productCategoryRepository.existsByName(productCategory.getName())) {
            throw new RuntimeException("Product Category name already exists");
        }
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory updateProductCategory(String id, ProductCategory productCategory) {
        ProductCategory existingProductCategory = getProductCategoryById(id);
        if (!existingProductCategory.getName().equals(productCategory.getName()) &&
                productCategoryRepository.existsByName(productCategory.getName())) {
            throw new RuntimeException("Product Category name already exists");
        }
        existingProductCategory.setName(productCategory.getName());
        return productCategoryRepository.save(existingProductCategory);
    }

    @Override
    public void deleteProductCategory(String id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public List<ProductCategoryDto> findAll() {
        return productCategoryRepository.findAll().stream().map(item -> {
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            productCategoryDto.setName(item.getName());
            productCategoryDto.setId(item.getId());
            return productCategoryDto;
        }).collect(Collectors.toList());
    }
}