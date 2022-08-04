package com.jovinn.capstoneproject.dto.client.request;

import com.jovinn.capstoneproject.enumerable.SkillLevel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillRequest {
    @NotBlank(message = "Tên kỹ năng không được để trống")
    String name;

    SkillLevel level;

    @NotBlank(message = "Mô tả ngắn về kỹ năng không được để trống")
    @Size(min = 1)
    String shortDescribe;

    @NotNull
    UUID userId;
}
