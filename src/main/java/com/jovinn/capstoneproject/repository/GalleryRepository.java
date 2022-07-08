package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface GalleryRepository extends JpaRepository<Gallery, UUID> {
}
