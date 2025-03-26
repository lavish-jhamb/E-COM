package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.ProfileDTO;
import com.ecomhub.user.service.exception.UserProfileNotFoundException;
import com.ecomhub.user.service.entity.Profile;
import com.ecomhub.user.service.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper mapper;

    public Profile getProfile(long id) {
        return profileRepository.findByUser_Id(id)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + id));
    }

    public ProfileDTO updateProfile(long id, ProfileDTO profileDTO) {

        Profile profile = profileRepository.findByUser_Id(id)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found: " + id));

        if (profileDTO.getPhone() != null) {
            profile.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getProfilePicture() != null) {
            profile.setProfilePicture(profileDTO.getProfilePicture());
        }

        Profile updatedProfile = profileRepository.save(profile);

        log.info("User profile updated successfully with id: {} ", id);

        return mapper.map(updatedProfile, ProfileDTO.class);
    }

}
