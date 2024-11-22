package vn.com.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.shop.services.ProductImageService;

import java.io.IOException;

@Controller
@RequestMapping("/product-image")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> view(@PathVariable String id) throws IOException {
        return productImageService.getFileResource(id);
    }
}
