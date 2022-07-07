package com.jovinn.capstoneproject.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestRequest {
    @NotNull
    UUID categoryId;
    @NotNull
    UUID subcategoryId;
    @NotBlank
    String recruitLevel;
    List<String> skillsName;
    @NotBlank
    String jobTitle;
    @NotBlank
    String shortRequirement;
    String attachFile;
    List<MilestoneContract> milestoneContracts;
    @NotNull
    Integer contractCancelFee;
    @NotNull
    Double budget;
    List<User> invitedUsers;
}
