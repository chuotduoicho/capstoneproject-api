package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {
}
