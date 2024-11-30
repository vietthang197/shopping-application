package vn.com.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Orders;
import vn.com.shop.entity.Product;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductService;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam String productId,
                                       @RequestParam(defaultValue = "1") Integer quantity,
                                       HttpSession session) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isFullyAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
            // Check if user is authenticated
            if (!isFullyAuthenticated) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("redirectUrl", "/login"));
            }

            // Validate product
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Validate quantity
            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Số lượng phải lớn hơn 0"
                        ));
            }

            // Check stock
            if (product.getQuantity() < quantity) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Số lượng vượt quá hàng tồn kho"
                        ));
            }

            // Get or create cart
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                cart = new ShoppingCart();
                session.setAttribute("cart", cart);
            }

            // Add to cart
            cart.addItem(product, quantity);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "totalItems", cart.getTotalItems(),
                    "message", "Thêm vào giỏ hàng thành công"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
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

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItem(@RequestParam String productId,
                                            @RequestParam Integer quantity,
                                            HttpSession session) {
        try {

            // Validate quantity
            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Số lượng phải lớn hơn 0"
                        ));
            }

            // Kiểm tra sản phẩm tồn tại
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Kiểm tra số lượng tồn kho
            if (product.getQuantity() < quantity) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Số lượng vượt quá hàng tồn kho"
                        ));
            }

            // Cập nhật giỏ hàng
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Giỏ hàng không tồn tại"
                        ));
            }

            cart.updateQuantity(productId, quantity);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "totalItems", cart.getTotalItems(),
                    "message", "Cập nhật thành công"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<?> removeCartItem(@RequestParam String productId,
                                            HttpSession session) {
        try {

            // Lấy giỏ hàng từ session
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Giỏ hàng không tồn tại"
                        ));
            }

            // Xóa sản phẩm
            cart.removeItem(productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "totalItems", cart.getTotalItems(),
                    "message", "Xóa sản phẩm thành công"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
}
