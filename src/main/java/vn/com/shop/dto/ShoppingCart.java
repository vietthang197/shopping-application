package vn.com.shop.dto;

import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private Map<String, CartItem> items = new ConcurrentHashMap<>();

    public void addItem(Product product) {
        CartItem item = items.get(product.getId());

        if (item == null) {
            item = new CartItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getSellPrice());
            item.setQuantity(1);
            Optional<ProductImage> avatarOptional = product.getProductImages().stream().filter(p -> "Y".equalsIgnoreCase(p.getIsAvatar())).findFirst();
            item.setImageUrl(avatarOptional.map(productImage -> "/product-image/view/" + productImage.getId()).orElse(null));
            items.put(product.getId(), item);
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateQuantity(String productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public int getTotalItems() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal(0);
        for (CartItem item : items.values()) {
            total = total.add(item.getPrice());
        }
        return total;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<String, CartItem> items) {
        this.items = items;
    }
}
