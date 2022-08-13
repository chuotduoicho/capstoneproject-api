package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.SkillRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.SellerSkillResponse;
import com.jovinn.capstoneproject.dto.client.response.SkillResponse;
import com.jovinn.capstoneproject.enumerable.SkillLevel;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.SkillRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Override
    public SkillResponse addSkill(SkillRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
        if(seller.getUser().getId().equals(currentUser.getId())) {
            Skill skill = new Skill(request.getName(), request.getLevel(),
                    request.getShortDescribe(), seller);
            Skill newSkill = skillRepository.save(skill);
            return new SkillResponse(newSkill.getId(), newSkill.getName(),
                    newSkill.getLevel(), newSkill.getShortDescribe(),
                    newSkill.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public SkillResponse update(UUID id, SkillRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy kỹ năng"));
        if(skill.getSeller().getUser().getId().equals(currentUser.getId())) {
            skill.setName(request.getName());
            skill.setLevel(request.getLevel());
            skill.setShortDescribe(request.getShortDescribe());
            skill.setUpdatedAt(new Date());
            skill.setSeller(seller);
            Skill update = skillRepository.save(skill);
            return new SkillResponse(update.getId(), update.getName(),
                    update.getLevel(), update.getShortDescribe(),
                    update.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy kỹ năng"));
        if (skill.getSeller().getUser().getId().equals(currentUser.getId())) {
            skillRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Xóa kỹ năng thành công");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<SellerSkillResponse> getSellerBySkillNameAndSkillLevelOrderBySellerId(List<String> name, SkillLevel level) {
        List<Skill> skills = skillRepository.findAllByNameInAndLevelOrderBySeller_Id(name,level);
        SellerSkillResponse sellerSkillResponse ;
        List<SellerSkillResponse> sellerSkillResponses = new ArrayList<>();
        UUID idTest = UUID.randomUUID();
        List<Skill> skills1 ;
        for (Skill skill : skills){
            if (!idTest.toString().equals(skill.getSeller().getId().toString())){
                sellerSkillResponse = new SellerSkillResponse();
                idTest = skill.getSeller().getId();
                skills1 = skillRepository.findAllBySeller_Id(skill.getSeller().getId());
                sellerSkillResponse.setSellerId(skill.getSeller().getId());
                sellerSkillResponse.setFirstName(skill.getSeller().getUser().getFirstName());
                sellerSkillResponse.setLastName(skill.getSeller().getUser().getLastName());
                sellerSkillResponse.setCity(skill.getSeller().getUser().getCity());
                sellerSkillResponse.setAvatar(skill.getSeller().getUser().getAvatar());
                sellerSkillResponse.setSkills(skills1);
                sellerSkillResponses.add(sellerSkillResponse);
            }
        }
        return sellerSkillResponses;
    }

    @Override
    public List<Skill> getAllSkillBySellerId(UUID id) {
        return skillRepository.findAllBySeller_Id(id);
    }

    @Override
    public List<Skill> getSellerBySkillNameAndSkillLevel(List<String> name, SkillLevel level) {
        return skillRepository.findAllByNameInAndLevelOrderBySeller_Id(name,level);
    }
}
