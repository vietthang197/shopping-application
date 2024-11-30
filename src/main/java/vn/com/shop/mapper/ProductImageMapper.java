package vn.com.shop.mapper;

import vn.com.shop.dto.ProductImageDto;
import vn.com.shop.entity.ProductImage;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductImageMapper {
    public static ProductImageDto toDto(ProductImage productImage) {
        if (productImage == null)
            return null;
        return ProductImageDto.builder()
                .id(productImage.getId())
                .fileName(productImage.getFileName())
                .filePath(productImage.getFilePath())
                .fileSize(productImage.getFileSize())
                .isAvatar(productImage.getIsAvatar())
                .build();
    }

    public static Set<ProductImageDto> toSetDto(Set<ProductImage> productImages) {
        if (productImages == null)
            return null;
        return productImages.stream().map(ProductImageMapper::toDto).collect(Collectors.toSet());
    }
}
