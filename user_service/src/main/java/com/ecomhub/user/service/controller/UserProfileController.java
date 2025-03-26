package com.ecomhub.user.service.controller;

import com.ecomhub.user.service.dto.UserProfileDTO;
import com.ecomhub.user.service.model.UserPrincipal;
import com.ecomhub.user.service.model.UserProfile;
import com.ecomhub.user.service.response.ApiResponse;
import com.ecomhub.user.service.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<UserProfile>> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("request received to fetch user profile");

        long id = userPrincipal.getId();
        UserProfile userProfile = userProfileService.getProfile(id);

        ApiResponse<UserProfile> response = new ApiResponse<>(
                true,
                "User profile fetched successfully with id: " + id,
                userProfile
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UserProfileDTO userProfileDTO) {
        log.info("request received to update user profile");
        log.info("user profile: {}", userProfileDTO);

        long id = userPrincipal.getId();
        UserProfileDTO userProfile = userProfileService.updateProfile(id, userProfileDTO);

        ApiResponse<UserProfileDTO> response = new ApiResponse<>(
                true,
                "User profile updated successfully with id: " + id,
                userProfile
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
