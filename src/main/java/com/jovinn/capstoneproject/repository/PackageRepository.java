package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {
    Package findPackageByBoxId(UUID boxId);
}
