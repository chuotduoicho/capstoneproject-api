package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.SkillMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillMetaDataRepository extends JpaRepository<SkillMetaData, UUID> {
    List<SkillMetaData> findAllBySubCategoryId(UUID subCategoryId);
}
