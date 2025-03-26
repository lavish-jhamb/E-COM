package com.ecomhub.user.service.repository;

import com.ecomhub.user.service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    // Fetch user profile by user ID
    Optional<Profile> findByUser_Id(long id);
}
