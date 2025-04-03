package com.ecomhub.product.service.service.seller;

import com.ecomhub.product.service.dto.ProductDTO;
import com.ecomhub.product.service.entity.Category;
import com.ecomhub.product.service.entity.Product;
import com.ecomhub.product.service.exception.CategoryNotFoundException;
import com.ecomhub.product.service.exception.ProductNotFoundException;
import com.ecomhub.product.service.repository.CategoryRepository;
import com.ecomhub.product.service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, int sellerId) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setProductImage(productDTO.getProductImage());
        product.setDiscount(productDTO.getDiscount());
        product.setSku(productDTO.getSku());
        product.setSellerId(sellerId);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("category not found with id: " + productDTO.getCategoryId()));

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return mapper.map(savedProduct, ProductDTO.class);
    }

    public List<ProductDTO> getProductsBySeller(int sellerId) {
        return productRepository.findBySellerId(sellerId)
                .stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .toList();
    }

    public void deleteProduct(int id, int sellerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id: " + id));

        if (product.getSellerId() != sellerId) {
            throw new ProductNotFoundException("product is not associated with seller id: " + id);
        }

        productRepository.delete(product);
    }
}
