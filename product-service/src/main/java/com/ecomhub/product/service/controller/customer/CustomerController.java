package com.ecomhub.product.service.controller.customer;

import com.ecomhub.product.service.dto.ProductDTO;
import com.ecomhub.product.service.response.ApiResponse;
import com.ecomhub.product.service.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = customerService.getAllProducts();

        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("products retrieved successfully");
        response.setData(products);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable("id") int id) {
        ProductDTO productDTO = customerService.getProductById(id);

        ApiResponse<ProductDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("product retrieved successfully by id " + id);
        response.setData(productDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
