package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;

import java.util.UUID;

public interface SkillService {
    Skill updateSkill(UUID sellerId, Seller seller, Skill skill);
}
