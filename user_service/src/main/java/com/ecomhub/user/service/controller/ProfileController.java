package com.ecomhub.user.service.controller;

import com.ecomhub.user.service.dto.ProfileDTO;
import com.ecomhub.user.service.entity.UserPrincipal;
import com.ecomhub.user.service.entity.Profile;
import com.ecomhub.user.service.response.ApiResponse;
import com.ecomhub.user.service.service.ProfileService;
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
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Profile>> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("request received to fetch user profile");

        long id = userPrincipal.getId();
        Profile profile = profileService.getProfile(id);

        ApiResponse<Profile> response = new ApiResponse<>(
                true,
                "User profile fetched successfully with id: " + id,
                profile
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProfileDTO profileDTO) {
        log.info("request received to update user profile");
        log.info("user profile: {}", profileDTO);

        long id = userPrincipal.getId();
        ProfileDTO userProfile = profileService.updateProfile(id, profileDTO);

        ApiResponse<ProfileDTO> response = new ApiResponse<>(
                true,
                "User profile updated successfully with id: " + id,
                userProfile
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
