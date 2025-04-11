package com.ecomhub.product.service.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private Double price;
    private String productImage;
    private Double discount;
    private Integer sku;
    private Integer categoryId;
}
