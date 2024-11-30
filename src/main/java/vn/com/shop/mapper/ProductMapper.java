package vn.com.shop.mapper;

import vn.com.shop.dto.ProductDto;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;

import java.util.Optional;
import java.util.Set;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .createdDt(product.getCreatedDt())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .buyPrice(product.getBuyPrice())
                .sellPrice(product.getSellPrice())
                .productImages(ProductImageMapper.toSetDto(product.getProductImages()))
                .productCategory(ProductCategoryMapper.toDto(product.getProductCategory()))
                .build();

        Set<ProductImage> productImageSet= product.getProductImages();
        if (productImageSet != null) {
            Optional<ProductImage> avatarOptional = productImageSet.stream().filter(item -> "Y".equalsIgnoreCase(item.getIsAvatar())).findFirst();
            avatarOptional.ifPresent(productImage -> productDto.setAvatar(ProductImageMapper.toDto(productImage)));
        }
        return productDto;
    }

}
