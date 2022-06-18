package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {
}
