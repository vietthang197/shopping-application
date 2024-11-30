package vn.com.shop.dto;

import lombok.*;
import vn.com.shop.entity.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String id;

    private String name;

    private Integer quantity;

    private ProductCategoryDto productCategory;

    private BigDecimal sellPrice; // gia ban

    private BigDecimal buyPrice; // gia nhap

    private ProductImageDto avatar;

    private Set<ProductImageDto> productImages;

    private String sku;

    private LocalDateTime createdDt;

    private String description;
}
