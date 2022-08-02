package com.jovinn.capstoneproject.dto.adminsite;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillMetaDataResponse {
    UUID id;
    UUID subCategoryId;
    String name;

    public SkillMetaDataResponse(UUID id, UUID subCategoryId, String name) {
        this.id = id;
        this.subCategoryId = subCategoryId;
        this.name = name;
    }
}
