package vn.com.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.com.shop.entity.ProductCategory;
import vn.com.shop.entity.ProductImage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String id;

    private String name;

    private Integer quantity;

    private ProductCategory productCategory;

    private BigDecimal sellPrice; // gia ban

    private BigDecimal buyPrice; // gia nhap

    private ProductImage avatar;

    private Set<ProductImage> productImages;

    private String sku;

    private LocalDateTime createdDt;

    private String description;
}
