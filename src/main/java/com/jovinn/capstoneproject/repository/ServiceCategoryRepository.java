package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {
    ServiceCategory findByName(String name);
}
