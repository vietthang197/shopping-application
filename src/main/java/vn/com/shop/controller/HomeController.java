package vn.com.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.shop.dto.ProductDto;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;
import vn.com.shop.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping
    public String home(Model model) {
        List<Product> popularProduct = productService.findPopularProduct();
        List<ProductDto> productDtoList = popularProduct.stream().map(product -> {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setProductCategory(product.getProductCategory());
            productDto.setBuyPrice(product.getBuyPrice());
            productDto.setSellPrice(product.getSellPrice());
            productDto.setQuantity(product.getQuantity());
            productDto.setCreatedDt(product.getCreatedDt());
            productDto.setSku(product.getSku());
            productDto.setProductImages(product.getProductImages());

            ProductImage avatar = product.getProductImages().stream().filter(item -> item.getIsAvatar().equals("Y")).findFirst().orElse(null);
            productDto.setAvatar(avatar);
            return productDto;
        }).collect(Collectors.toList());
        model.addAttribute("popularProduct", productDtoList);
        return "home";
    }
}
