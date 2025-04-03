package com.ecomhub.product.service.service.customer;

import com.ecomhub.product.service.dto.ProductDTO;
import com.ecomhub.product.service.exception.ProductNotFoundException;
import com.ecomhub.product.service.repository.CategoryRepository;
import com.ecomhub.product.service.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .toList();
    }

    public ProductDTO getProductById(int id) {
        return productRepository.findById(id)
                .map(product -> mapper.map(product, ProductDTO.class))
                .orElseThrow(() -> new ProductNotFoundException("product not found with id: " + id));
    }

}
