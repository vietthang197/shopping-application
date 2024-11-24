package vn.com.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItem implements Serializable {
    private String productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;

    public BigDecimal getSubTotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
