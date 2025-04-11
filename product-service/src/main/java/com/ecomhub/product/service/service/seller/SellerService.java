package com.ecomhub.product.service.service.seller;

import com.ecomhub.product.service.dto.ProductDTO;
import com.ecomhub.product.service.entity.Category;
import com.ecomhub.product.service.entity.Product;
import com.ecomhub.product.service.exception.CategoryNotFoundException;
import com.ecomhub.product.service.exception.ImageNotPresentException;
import com.ecomhub.product.service.exception.ImageNotProcessedException;
import com.ecomhub.product.service.exception.ProductNotFoundException;
import com.ecomhub.product.service.repository.CategoryRepository;
import com.ecomhub.product.service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SellerService {

    private static final Logger log = LoggerFactory.getLogger(SellerService.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${image.upload-dir}")
    String imagePath;

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

    public String uploadImage(MultipartFile image) {

        Path uploadPath = Paths.get(imagePath);
        if (!Files.exists(uploadPath)) {
            try {
                log.info("Building directory for storing image....");
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new FileSystemNotFoundException("Error creating/finding directory provided in the path!"); //error creating the file storage path
            }
        }

        if (image == null) {
            throw new ImageNotPresentException("Image not provided,Please provide valid image!");
        }

        String originalFilename = image.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf('.'));
        Path filePath = uploadPath.resolve(fileName);
        try {
            log.info("Copying image from source to destination...");
            Files.copy(image.getInputStream(), filePath);
        } catch (IOException e) {
            throw new ImageNotProcessedException("An exception encountered while saving the image");// error while saveing the file
        }

        return filePath.toString();


    }

    public ProductDTO updateProduct(int productId, ProductDTO productDTO, int sellerId) {

        log.info("Retrieving product details..");

        Product productDetails = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product with given id:" + productId + " doesn't exists"));
        if (productDetails.getSellerId() != sellerId) {
            throw new ProductNotFoundException("product is not associated with seller id: " + sellerId);
        }

        log.info("Retrieving category details.. ");

        Optional<Integer> categoryId = Optional.ofNullable(productDTO.getCategoryId());
        if (categoryId.isPresent()) {
            Category category = categoryRepository.findById(categoryId.get())
                    .orElseThrow(() -> new CategoryNotFoundException("Category with given id:" + categoryId + " doesn't exists!"));

            productDetails.setCategory(category);
        }

        log.info("Updating product details for product: {}", productId);

        Optional.ofNullable(productDTO.getName()).ifPresent(productDetails::setName);
        Optional.ofNullable(productDTO.getDescription()).ifPresent(productDetails::setDescription);
        Optional.ofNullable(productDTO.getDiscount()).ifPresent(productDetails::setDiscount);
        Optional.ofNullable(productDTO.getSku()).ifPresent(productDetails::setSku);
        Optional.ofNullable(productDTO.getPrice()).ifPresent(productDetails::setPrice);

        Product updatedProduct = productRepository.save(productDetails);
        log.info(" product details updated successfully with id: {} ", productId);

        return mapper.map(updatedProduct, ProductDTO.class);
    }
}
