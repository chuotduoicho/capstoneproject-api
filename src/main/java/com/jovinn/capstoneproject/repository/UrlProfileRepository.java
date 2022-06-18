package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.UrlProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlProfileRepository extends JpaRepository<UrlProfile, UUID> {
}
