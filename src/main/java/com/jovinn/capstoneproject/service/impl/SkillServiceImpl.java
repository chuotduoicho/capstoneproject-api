package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.repository.SkillRepository;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    private SkillRepository skillRepository;
    @Override
    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }
}
