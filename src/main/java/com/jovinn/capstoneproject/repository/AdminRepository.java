package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
