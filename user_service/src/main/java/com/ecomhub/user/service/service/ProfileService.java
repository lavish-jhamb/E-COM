package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.ProfileDTO;
import com.ecomhub.user.service.exception.ProfileNotFoundException;
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

    public Profile getProfile(int id) {
        return profileRepository.findByAccount_Id(id)
                .orElseThrow(() -> new ProfileNotFoundException("account profile not found with id: " + id));
    }

    public ProfileDTO updateProfile(int id, ProfileDTO profileDTO) {

        Profile profile = profileRepository.findByAccount_Id(id)
                .orElseThrow(() -> new ProfileNotFoundException("account profile not found with id: " + id));

        if (profileDTO.getPhone() != null) {
            profile.setPhone(profileDTO.getPhone());
        }
        if (profileDTO.getProfilePicture() != null) {
            profile.setProfilePicture(profileDTO.getProfilePicture());
        }

        Profile updatedProfile = profileRepository.save(profile);

        log.info("account profile updated successfully with id: {} ", id);

        return mapper.map(updatedProfile, ProfileDTO.class);
    }

}
