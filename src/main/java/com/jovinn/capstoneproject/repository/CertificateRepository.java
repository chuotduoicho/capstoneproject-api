package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
}
