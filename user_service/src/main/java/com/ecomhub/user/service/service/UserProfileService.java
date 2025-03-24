package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.UserProfileDTO;
import com.ecomhub.user.service.exception.UserProfileNotFoundException;
import com.ecomhub.user.service.model.UserProfile;
import com.ecomhub.user.service.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ModelMapper mapper;

    public UserProfile getProfile(long id) {
        return userProfileRepository.findByUser_Id(id)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + id));
    }

    public UserProfileDTO updateProfile(long id, UserProfileDTO userProfileDTO) {

        UserProfile userProfile = userProfileRepository.findByUser_Id(id)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + id));

        if (userProfileDTO.getPhone() != null) {
            userProfile.setPhone(userProfileDTO.getPhone());
        }
        if (userProfileDTO.getProfilePicture() != null) {
            userProfile.setProfilePicture(userProfileDTO.getProfilePicture());
        }

        UserProfile updatedUserProfile = userProfileRepository.save(userProfile);

        log.info("User profile updated successfully with id: {} ", id);

        return mapper.map(updatedUserProfile, UserProfileDTO.class);
    }

}
