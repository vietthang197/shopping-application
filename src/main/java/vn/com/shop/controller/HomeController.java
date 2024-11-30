package vn.com.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.dto.ProductDto;
import vn.com.shop.entity.Product;
import vn.com.shop.mapper.ProductMapper;
import vn.com.shop.services.ProductCategoryService;
import vn.com.shop.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    @GetMapping
    public String home(Model model) {

        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();

        List<Product> popularProduct = productService.findPopularProduct();
        List<ProductDto> productDtoList = popularProduct.stream().map(ProductMapper::toDto).collect(Collectors.toList());

        for (ProductCategoryDto productCategoryDto : productCategoryDtos) {
            List<Product> products = productService.findByProductCategoryLimit(productCategoryDto.getId(), 3);
            productCategoryDto.setProducts(products.stream().map(ProductMapper::toDto).collect(Collectors.toList()));
        }

        model.addAttribute("popularProduct", productDtoList);
        model.addAttribute("categories", productCategoryDtos);
        return "home";
    }

    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isEmpty()) {
            return "404";
        } else {
            Product product = productOptional.get();
            ProductDto productDto = ProductMapper.toDto(product);
            model.addAttribute("product", productDto);
            return "product/product-detail";
        }
    }
}
