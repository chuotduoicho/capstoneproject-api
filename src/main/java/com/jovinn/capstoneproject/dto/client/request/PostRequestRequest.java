package com.jovinn.capstoneproject.dto.client.request;

import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
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
    @Size(max = 50, message = "Chỉ được nhập tối đa 50 ký tự")
    String jobTitle;
    @NotBlank(message = "Không được để trống")
    @Size(max = 255, message = "Mô tả ngắn gọn chỉ được nhập tối đa 255 ký tự")
    String shortRequirement;
    String attachFile;
    PostRequestStatus status;
    List<MilestoneContract> milestoneContracts;
    @NotNull(message = "Không được để trống, tối thiểu là 0% và tối đa là 100%")
    @Min(value = 0, message = "Không được nhỏ hơn 0")
    @Max(value = 100, message = "Không được vượt quá 100")
    @Size(max = 3)
    Integer contractCancelFee;
//    @NotNull
//    BigDecimal budget;
    List<User> invitedUsers;
}
