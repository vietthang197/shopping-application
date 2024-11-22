package vn.com.shop.services;

import org.springframework.web.multipart.MultipartFile;
import vn.com.shop.config.FileStorageProperties;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    void deleteFile(String fileName);
}
