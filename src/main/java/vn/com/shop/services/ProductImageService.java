package vn.com.shop.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    ProductImage saveProductAvatar(Product product, MultipartFile file);
    ProductImage saveProductImage(Product product, MultipartFile file);
    void deleteProductImage(String imageId);
    List<ProductImage> getProductImages(Product product);

    ProductImage getProductAvatar(Product product);

    List<ProductImage> getProductSubImages(Product product);

    ResponseEntity<Resource> getFileResource(String id) throws IOException;
}
