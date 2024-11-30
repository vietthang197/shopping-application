package vn.com.shop.mapper;

import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.entity.ProductCategory;

public class ProductCategoryMapper {
    public static ProductCategoryDto toDto(ProductCategory productCategory) {
        return ProductCategoryDto.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .build();
    }
}
