package com.ecomhub.product.service.controller.seller;

import com.ecomhub.product.service.dto.ProductDTO;
import com.ecomhub.product.service.response.ApiResponse;
import com.ecomhub.product.service.service.seller.SellerService;
import com.ecomhub.security.model.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller/products")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal Principal principal) {

        ProductDTO product = sellerService.createProduct(productDTO, principal.getId());

        ApiResponse<ProductDTO> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("product created successfully");
        response.setData(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsBySeller(@AuthenticationPrincipal Principal principal) {

        List<ProductDTO> products = sellerService.getProductsBySeller(principal.getId());

        ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("products retrieved successfully");
        response.setData(products);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int id, @AuthenticationPrincipal Principal principal) {
        sellerService.deleteProduct(id, principal.getId());

        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("product deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
