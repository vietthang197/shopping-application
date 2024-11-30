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
import vn.com.shop.entity.ProductImage;
import vn.com.shop.mapper.ProductCategoryMapper;
import vn.com.shop.mapper.ProductImageMapper;
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
        List<ProductDto> productDtoList = popularProduct.stream().map(product -> {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setProductCategory(ProductCategoryMapper.toDto(product.getProductCategory()));
            productDto.setBuyPrice(product.getBuyPrice());
            productDto.setSellPrice(product.getSellPrice());
            productDto.setQuantity(product.getQuantity());
            productDto.setCreatedDt(product.getCreatedDt());
            productDto.setSku(product.getSku());
            productDto.setProductImages(ProductImageMapper.toSetDto(product.getProductImages()));

            ProductImage avatar = product.getProductImages().stream().filter(item -> item.getIsAvatar().equals("Y")).findFirst().orElse(null);
            productDto.setAvatar(ProductImageMapper.toDto(avatar));
            return productDto;
        }).collect(Collectors.toList());

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
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isEmpty()) {
            return "404";
        } else {
            Product product = productOptional.get();
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setProductCategory(ProductCategoryMapper.toDto(product.getProductCategory()));
            productDto.setBuyPrice(product.getBuyPrice());
            productDto.setSellPrice(product.getSellPrice());
            productDto.setQuantity(product.getQuantity());
            productDto.setCreatedDt(product.getCreatedDt());
            productDto.setSku(product.getSku());
            productDto.setProductImages(ProductImageMapper.toSetDto(product.getProductImages()));

            ProductImage avatar = product.getProductImages().stream().filter(item -> item.getIsAvatar().equals("Y")).findFirst().orElse(null);
            productDto.setAvatar(ProductImageMapper.toDto(avatar));
            model.addAttribute("product", productDto);
            return "product/product-detail";
        }
    }
}
