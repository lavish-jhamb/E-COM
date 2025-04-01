package com.ecomhub.user.service.controller;

import com.ecomhub.user.service.dto.AddressDTO;
import com.ecomhub.user.service.dto.UserPrincipal;
import com.ecomhub.user.service.response.ApiResponse;
import com.ecomhub.user.service.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/profile/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getProfileAddress(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("request received to fetch addresses for user : {}", userPrincipal.getUsername());

        int accountId = userPrincipal.getId();
        List<AddressDTO> addresses = addressService.getAddress(accountId);

        ApiResponse<List<AddressDTO>> response = new ApiResponse<>(
                true,
                "addresses fetched successfully for user : " + userPrincipal.getUsername(),
                addresses
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<AddressDTO>> createProfileAddress(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody AddressDTO addressDTO) {
        log.info("request received to create address for user : {}", userPrincipal.getUsername());

        int accountId = userPrincipal.getId();
        AddressDTO address = addressService.createAddress(accountId, addressDTO);

        ApiResponse<AddressDTO> response = new ApiResponse<>(
                true,
                "address created successfully for user : " + userPrincipal.getUsername(),
                address
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<AddressDTO>> updateProfileAddress(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                               @PathVariable int id, @RequestBody AddressDTO addressDTO) {
        log.info("request received to update address with id : {}", id);

        int accountId = userPrincipal.getId();
        AddressDTO address = addressService.updateAddress(accountId, id, addressDTO);

        ApiResponse<AddressDTO> response = new ApiResponse<>(
                true,
                "address updated successfully for user : " + userPrincipal.getUsername(),
                address
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> deleteProfileAddress(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int id) {
        log.info("request received to delete address with id : {}", id);

        int accountId = userPrincipal.getId();
        addressService.deleteAddress(accountId, id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "address deleted successfully for user : " + userPrincipal.getUsername()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
