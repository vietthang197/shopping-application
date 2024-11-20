package vn.com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @Column(name = "sell_price", precision = 19)
    private BigDecimal sellPrice; // gia ban

    @Column(name = "buy_price", precision = 19)
    private BigDecimal buyPrice; // gia nhap

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private Set<ProductImage> productImages = new LinkedHashSet<>();

    @Column(name = "sku")
    private String sku;

}