package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.EducationRequest;
import com.jovinn.capstoneproject.dto.request.SkillRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.EducationResponse;
import com.jovinn.capstoneproject.dto.response.SkillResponse;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface SkillService {
    Skill updateSkill(UUID id, Seller seller, Skill skill);
    SkillResponse addSkill(SkillRequest request, UserPrincipal currentUser);
    SkillResponse update(UUID id, SkillRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
