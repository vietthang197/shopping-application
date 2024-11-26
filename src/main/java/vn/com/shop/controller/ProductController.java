package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;
import vn.com.shop.services.ProductImageService;
import vn.com.shop.services.ProductService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        return "product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        return "product/form";
    }

    @PostMapping("/create")
    public String createProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "avatarImage", required = false) MultipartFile avatarImage,
            @RequestParam(value = "productImage", required = false) MultipartFile[] productImage
    ) {
        // Save product first
        Product savedProduct = productService.createProduct(product);

        // Save avatar if uploaded
        if (avatarImage != null && !avatarImage.isEmpty()) {
            productImageService.saveProductAvatar(savedProduct, avatarImage);
        }

        // Save additional images
        if (productImage != null) {
            for (MultipartFile image : productImage) {
                if (!image.isEmpty()) {
                    productImageService.saveProductImage(savedProduct, image);
                }
            }
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        ProductImage avatar = productImageService.getProductAvatar(product);
        List<ProductImage> additionalImages = productImageService.getProductSubImages(product);

        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("avatar", avatar);
        model.addAttribute("additionalImages", additionalImages);

        return "product/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable String id,
            @ModelAttribute Product product,
            @RequestParam(value = "avatarImage", required = false) MultipartFile avatarImage,
            @RequestParam(value = "productImages", required = false) MultipartFile[] productImages
    ) {
        // Update product info
        Product updatedProduct = productService.updateProduct(id, product);

        // Update avatar if new image uploaded
        if (avatarImage != null && !avatarImage.isEmpty()) {
            productImageService.saveProductAvatar(updatedProduct, avatarImage);
        }

        // Add additional images
        if (productImages != null) {
            for (MultipartFile image : productImages) {
                if (!image.isEmpty()) {
                    productImageService.saveProductImage(updatedProduct, image);
                }
            }
        }

        return "redirect:/products";
    }

    @GetMapping("/delete-image/{imageId}")
    public String deleteProductImage(@PathVariable String imageId,
                                     @RequestParam("productId") String productId) {
        productImageService.deleteProductImage(imageId);
        return "redirect:/products/edit/" + productId;
    }
}
