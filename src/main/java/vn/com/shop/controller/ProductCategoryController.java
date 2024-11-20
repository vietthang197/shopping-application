package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.shop.entity.ProductCategory;
import vn.com.shop.services.ProductCategoryService;

@Controller
@RequestMapping("/product-categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
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

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productCategory", new ProductCategory());
        return "product-category/form";
    }

    @PostMapping("/create")
    public String createProductCategory(@ModelAttribute ProductCategory productCategory) {
        productCategoryService.createProductCategory(productCategory);
        return "redirect:/product-categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(id);
        model.addAttribute("productCategory", productCategory);
        return "product-category/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProductCategory(@PathVariable String id, @ModelAttribute ProductCategory productCategory) {
        productCategoryService.updateProductCategory(id, productCategory);
        return "redirect:/product-categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductCategory(@PathVariable String id) {
        productCategoryService.deleteProductCategory(id);
        return "redirect:/product-categories";
    }
}
