package com.jovinn.capstoneproject.dto.request;

import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestRequest {
    @NotNull
    UUID categoryId;
    @NotNull
    UUID subCategoryId;
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
    BigDecimal budget;
    List<User> invitedUsers;
}
