package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.shop.entity.Product;
import vn.com.shop.entity.ProductCategory;
import vn.com.shop.repository.ProductRepository;
import vn.com.shop.services.ProductCategoryService;
import vn.com.shop.services.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<ProductCategory> getAllCategories() {
        return productCategoryService.getAllProductCategories();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product product) {
        Product existingProduct = getProductById(id);
        validateProductUpdate(id, product);

        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setProductCategory(product.getProductCategory());
        existingProduct.setSellPrice(product.getSellPrice());
        existingProduct.setBuyPrice(product.getBuyPrice());
        existingProduct.setSku(product.getSku());

        return productRepository.save(existingProduct);
    }

    private void validateProduct(Product product) {
        if (product.getQuantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
        if (product.getBuyPrice() != null && product.getBuyPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Buy price cannot be negative");
        }
        if (product.getSellPrice() != null && product.getSellPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Sell price cannot be negative");
        }
//        if (product.getSku() != null && productRepository.existsBySkuAndIdNot(product.getSku(), product.getId())) {
//            throw new RuntimeException("SKU already exists");
//        }
    }

    private void validateProductUpdate(String id, Product product) {
        validateProduct(product);
        if (!id.equals(product.getId()) && productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product with this ID already exists");
        }
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findPopularProduct() {
        Page<Product> productPage = productRepository.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdDt")));
        return productPage.getContent();
    }
}
