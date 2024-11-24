package vn.com.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Product;
import vn.com.shop.services.ProductService;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam String productId,
                                       HttpSession session) {
        // Lấy hoặc tạo giỏ hàng mới từ session
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        // Thêm sản phẩm vào giỏ hàng
        Product product = productService.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        cart.addItem(product);

        // Trả về số lượng items trong giỏ hàng để cập nhật UI
        return ResponseEntity.ok(Map.of(
                "totalItems", cart.getTotalItems(),
                "message", "Product added to cart successfully"
        ));
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "cart/view";
    }
}
