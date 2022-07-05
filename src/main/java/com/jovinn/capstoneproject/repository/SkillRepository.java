package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.dto.response.SellerSkillResponse;
import com.jovinn.capstoneproject.enumerable.SkillLevel;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Skill findSkillBySellerId(UUID sellerId);

    List<Skill> findAllByNameInAndLevelOrderBySeller_Id(List<String> name, SkillLevel level);

    List<Skill> findAllBySeller_Id(UUID id);

    List<Skill> findAllByNameIn(List<String> name);
}
