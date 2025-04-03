package com.ecomhub.product.service.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private String productImage;
    private double discount;
    private int sku;
    private int categoryId;
}
