package com.ecomhub.user.service.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private int zipcode;
    private String country;
}
