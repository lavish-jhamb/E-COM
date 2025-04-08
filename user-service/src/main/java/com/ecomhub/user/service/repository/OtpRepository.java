package com.ecomhub.user.service.repository;

import com.ecomhub.user.service.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OTP, String> {

}