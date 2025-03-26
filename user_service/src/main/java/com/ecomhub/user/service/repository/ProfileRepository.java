package com.ecomhub.user.service.repository;

import com.ecomhub.user.service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    // Fetch profile by account ID
    Optional<Profile> findByAccount_Id(int id);
}
