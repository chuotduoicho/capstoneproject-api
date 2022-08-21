package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {
    Package findPackageByBoxId(UUID boxId);
    @Query("SELECT p.box.id FROM Package p WHERE p.id = ?1")
    String getBoxIdByPackage(UUID packageId);
    List<Package> findAllByBoxIdOrderByPriceAsc(UUID boxId);
}
