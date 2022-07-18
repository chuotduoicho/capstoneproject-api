package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skill")
@CrossOrigin(origins = "*")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping("/getAllSkillBySellerId/{id}")
    public List<Skill> getAllSkillBySellerId(@PathVariable UUID id){
        return skillService.getAllSkillBySellerId(id);
    }
}
