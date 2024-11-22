package vn.com.shop.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductImage;
import vn.com.shop.repository.ProductImageRepository;
import vn.com.shop.services.FileStorageService;
import vn.com.shop.services.ProductImageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ProductImage saveProductAvatar(Product product, MultipartFile file) {
        // Delete existing avatar if exists
        List<ProductImage> existingAvatars = productImageRepository.findByProductAndIsAvatar(product, "Y");
        if (!CollectionUtils.isEmpty(existingAvatars)) {
            ProductImage existingAvatar = existingAvatars.getFirst();
            fileStorageService.deleteFile(existingAvatar.getFileName());
            productImageRepository.delete(existingAvatar);
        }

        // Save new avatar
        String filePath = fileStorageService.storeFile(file);
        ProductImage avatar = new ProductImage();
        avatar.setProduct(product);
        avatar.setFileName(FilenameUtils.getName(filePath));
        avatar.setFilePath(filePath);
        avatar.setFileSize(file.getSize());
        avatar.setIsAvatar("Y");

        return productImageRepository.save(avatar);
    }

    @Override
    public ProductImage saveProductImage(Product product, MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setFileName(FilenameUtils.getName(filePath));
        productImage.setFilePath(filePath);
        productImage.setFileSize(file.getSize());
        productImage.setIsAvatar("N");

        return productImageRepository.save(productImage);
    }

    @Override
    public void deleteProductImage(String imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        fileStorageService.deleteFile(image.getFileName());
        productImageRepository.delete(image);
    }

    @Override
    public List<ProductImage> getProductImages(Product product) {
        return productImageRepository.findByProduct(product);
    }

    @Override
    public ProductImage getProductAvatar(Product product) {
        List<ProductImage> productImages = productImageRepository.findByProductAndIsAvatar(product, "Y");
        if (!CollectionUtils.isEmpty(productImages))
            return productImages.getFirst();
        else return null;
    }

    @Override
    public List<ProductImage> getProductSubImages(Product product) {
        return productImageRepository.findByProductAndIsAvatar(product, "N");
    }

    @Override
    public ResponseEntity<Resource> getFileResource(String id) throws IOException {
        Optional<ProductImage> productImageOptional = productImageRepository.findById(id);
        if (productImageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ProductImage productImage = productImageOptional.get();
        InputStream is = Files.newInputStream(Paths.get(productImage.getFilePath()));
        InputStreamResource isr = new InputStreamResource(is);
        return ResponseEntity.status(HttpStatus.OK).body(isr);
    }
}
