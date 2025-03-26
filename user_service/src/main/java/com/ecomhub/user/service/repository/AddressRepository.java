package com.ecomhub.user.service.repository;

import com.ecomhub.user.service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    /**
     * This method navigates the relationships between the entities:
     * The Address entity has a reference to Profile (typically via a foreign key, profile_id).
     * The Profile entity has a reference to an Account (via another foreign key, account_id).
     * Hence, the method findByProfile_Account_Id(long accountId):
     * Uses the accountId of the logged-in user (via Account.id) to fetch all related addresses by navigating the relationships: Account → Profile → Address.
     */
    List<Address> findByProfile_Account_Id(int accountId);
}
