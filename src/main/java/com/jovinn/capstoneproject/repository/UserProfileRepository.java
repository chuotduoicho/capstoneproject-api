package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.dto.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    UserProfile findByUsername(String username);
}
