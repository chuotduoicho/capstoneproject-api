package com.jovinn.capstoneproject.dto.request;

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
    @NotBlank
    String name;
    @NotBlank
    SkillLevel level;
    @NotBlank
    @Size
    String shortDescribe;
    @NotNull
    UUID userId;
}
