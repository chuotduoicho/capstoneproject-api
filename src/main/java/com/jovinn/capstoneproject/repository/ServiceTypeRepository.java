package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {
    List<ServiceType> findBySubCategoryId(UUID subCategoryId);
}

