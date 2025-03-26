package com.ecomhub.user.service.repository;

import com.ecomhub.user.service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    // Fetch user profile by user ID
    Optional<UserProfile> findByUser_Id(long id);
}
