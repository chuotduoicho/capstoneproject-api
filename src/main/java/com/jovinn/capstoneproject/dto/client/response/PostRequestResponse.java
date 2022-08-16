package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.model.SkillMetaData;
import com.jovinn.capstoneproject.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
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
    List<SkillMetaData> skillsName;
    String jobTitle;
    String shortRequirement;
    String attachFile;
    List<MilestoneContract> milestoneContracts;
    Integer contractCancelFee;
    BigDecimal budget;
    UUID buyerId;
    String buyerFirstName;
    String buyerLastname;
    String city;
    Date createdAt;
    Date userCreateAt;
    Integer numberPostRequestCreated;

    List<UserProfile> invitedUser;

    public PostRequestResponse(UUID postRequestId, UUID categoryId, UUID subcategoryId, String recruitLevel,
                               List<SkillMetaData> skillsName, String jobTitle, String shortRequirement, String attachFile,
                               List<MilestoneContract> milestoneContracts, Integer contractCancelFee,
                               BigDecimal budget, List<UserProfile> invitedUser) {
        this.postRequestId = postRequestId;
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
        this.invitedUser = invitedUser;
    }

    public PostRequestResponse(UUID postRequestId, UUID categoryId, UUID subcategoryId, String jobTitle,
                               BigDecimal budget, UUID buyerId, String buyerFirstName, String buyerLastname, String attachFile,
                               String city, Date createdAt, String recruitLevel, List<SkillMetaData> skillsName,
                               String shortRequirement, List<MilestoneContract> milestoneContracts, Integer contractCancelFee) {
        this.postRequestId = postRequestId;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.jobTitle = jobTitle;
        this.budget = budget;
        this.buyerId = buyerId;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastname = buyerLastname;
        this.attachFile = attachFile;
        this.city = city;
        this.createdAt = createdAt;
        this.recruitLevel = recruitLevel;
        this.skillsName = skillsName;
        this.shortRequirement = shortRequirement;
        this.milestoneContracts = milestoneContracts;
        this.contractCancelFee = contractCancelFee;
    }

    public PostRequestResponse(String categoryName, String subCategoryName, String recruitLevel,
                               List<SkillMetaData> skillsName, String jobTitle, String shortRequirement,
                               List<MilestoneContract> milestoneContracts, Integer contractCancelFee,
                               BigDecimal budget, String buyerFirstName, String buyerLastname,
                               String city, Date userCreateAt, Integer numberPostRequestCreated) {
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
