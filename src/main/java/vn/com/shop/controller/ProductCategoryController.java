package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.dto.ProductDto;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductCategory;
import vn.com.shop.mapper.ProductMapper;
import vn.com.shop.services.ProductCategoryService;
import vn.com.shop.services.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/admin/product-categories")
    public String listProductCategories(Model model,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<ProductCategory> productCategoryPage = productCategoryService.getAllProductCategories(PageRequest.of(page, size));
        model.addAttribute("productCategories", productCategoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productCategoryPage.getTotalPages());
        model.addAttribute("totalItems", productCategoryPage.getTotalElements());
        return "product-category/list";
    }

    @GetMapping("/admin/product-categories/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productCategory", new ProductCategory());
        return "product-category/form";
    }

    @PostMapping("/admin/product-categories/create")
    public String createProductCategory(@ModelAttribute ProductCategory productCategory) {
        productCategoryService.createProductCategory(productCategory);
        return "redirect:/admin/product-categories";
    }

    @GetMapping("/admin/product-categories/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(id);
        model.addAttribute("productCategory", productCategory);
        return "product-category/form";
    }

    @PostMapping("/admin/product-categories/edit/{id}")
    public String updateProductCategory(@PathVariable String id, @ModelAttribute ProductCategory productCategory) {
        productCategoryService.updateProductCategory(id, productCategory);
        return "redirect:/admin/product-categories";
    }

    @GetMapping("/admin/product-categories/delete/{id}")
    public String deleteProductCategory(@PathVariable String id) {
        productCategoryService.deleteProductCategory(id);
        return "redirect:/admin/product-categories";
    }

    @GetMapping("/product-categories/product/{categoryId}")
    public String listProductsByCategory(@PathVariable String categoryId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "12") int size,
                                         Model model) {

        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);

        Optional<ProductCategory> productCategoryOptional = productCategoryService.findById(categoryId);
        if (productCategoryOptional.isEmpty()) {
            return "404";
        }
        ProductCategory category = productCategoryOptional.get();

        Page<Product> products = productService.getProductsByCategory(categoryId,
                PageRequest.of(page, size, Sort.by("createdDt").descending()));

        Page<ProductDto> productDtos = products.map(ProductMapper::toDto);

        model.addAttribute("category", category);
        model.addAttribute("products", productDtos);

        return "product/category";
    }
}
