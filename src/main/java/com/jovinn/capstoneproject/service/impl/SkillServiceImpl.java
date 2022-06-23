package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.SkillRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public Skill updateSkill(UUID id, Seller seller, Skill skill) {
        Skill existSkill = skillRepository.findSkillBySellerId(id);
        existSkill.setName(skill.getName());
        existSkill.setShortDescribe(skill.getShortDescribe());
        existSkill.setLevel(skill.getLevel());
        existSkill.setUpdatedAt(new Date());
        return skillRepository.save(existSkill);
    }
}
