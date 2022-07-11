package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.Skill;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestResponse {

    UUID postRequestId;

    UUID categoryId;

    String categoryName;

    UUID subcategoryId;
    String subCategoryName;
    String recruitLevel;
    List<Skill> skillsName;

    String jobTitle;

    String shortRequirement;
    String attachFile;
    List<MilestoneContract> milestoneContracts;

    Integer contractCancelFee;

    Double budget;

    UUID buyerId;
    String buyerFirstName;
    String buyerLastname;
    String city;
    Date createdAt;

    Date userCreateAt;

    Integer numberPostRequestCreated;

    public PostRequestResponse(UUID categoryId, UUID subcategoryId, String recruitLevel, List<Skill> skillsName, String jobTitle, String shortRequirement, String attachFile, List<MilestoneContract> milestoneContracts, Integer contractCancelFee, Double budget) {
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.recruitLevel = recruitLevel;
        this.skillsName = skillsName;
        this.jobTitle = jobTitle;
        this.shortRequirement = shortRequirement;
        this.attachFile = attachFile;
        this.milestoneContracts = milestoneContracts;
        this.contractCancelFee = contractCancelFee;
        this.budget = budget;
    }

    public PostRequestResponse(UUID postRequestId, String jobTitle, Double budget, UUID buyerId, String buyerFirstName,String buyerLastname, String city, Date createdAt) {
        this.postRequestId = postRequestId;
        this.jobTitle = jobTitle;
        this.budget = budget;
        this.buyerId = buyerId;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastname = buyerLastname;
        this.city = city;
        this.createdAt = createdAt;
    }

    public PostRequestResponse(String categoryName, String subCategoryName, String recruitLevel, List<Skill> skillsName, String jobTitle, String shortRequirement, List<MilestoneContract> milestoneContracts, Integer contractCancelFee, Double budget, String buyerFirstName, String buyerLastname, String city, Date userCreateAt, Integer numberPostRequestCreated) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.recruitLevel = recruitLevel;
        this.skillsName = skillsName;
        this.jobTitle = jobTitle;
        this.shortRequirement = shortRequirement;
        this.milestoneContracts = milestoneContracts;
        this.contractCancelFee = contractCancelFee;
        this.budget = budget;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastname = buyerLastname;
        this.city = city;
        this.userCreateAt = userCreateAt;
        this.numberPostRequestCreated = numberPostRequestCreated;
    }

    public PostRequestResponse(UUID postRequestId){
        this.postRequestId = postRequestId;
    }
}
