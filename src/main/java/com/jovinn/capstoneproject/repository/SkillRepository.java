package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Skill findSkillBySellerId(UUID sellerId);
}
