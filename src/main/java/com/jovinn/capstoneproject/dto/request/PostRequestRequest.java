package com.jovinn.capstoneproject.dto.request;

import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestRequest {
    @NotNull(message = "Không được để trống")
    UUID categoryId;
    @NotNull(message = "Không được để trống")
    UUID subCategoryId;
    @NotBlank(message = "Không được để trống")
    String recruitLevel;
    List<String> skillsName;
    @NotBlank(message = "Không được để trống")
    String jobTitle;
    @NotBlank(message = "Không được để trống")
    String shortRequirement;
    String attachFile;
    List<MilestoneContract> milestoneContracts;
    @NotNull(message = "Không được để trống, tối thiểu là 0% và tối đa là 100%")
    @Min(value = 0, message = "Không được nhỏ hơn 0")
    @Max(value = 100, message = "Không được vượt quá 100")
    Integer contractCancelFee;
//    @NotNull
//    BigDecimal budget;
    List<User> invitedUsers;
}
