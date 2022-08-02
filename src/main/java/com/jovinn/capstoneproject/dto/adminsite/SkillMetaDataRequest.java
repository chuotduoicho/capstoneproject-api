package com.jovinn.capstoneproject.dto.adminsite;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillMetaDataRequest {
    @NotBlank(message = "Không được để trống tên kỹ năng")
    String name;
    @NotNull UUID subCategoryId;
}
