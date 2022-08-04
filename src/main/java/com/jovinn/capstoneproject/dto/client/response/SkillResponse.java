package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.enumerable.SkillLevel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillResponse {
    @NotNull UUID id;
    String name;
    SkillLevel level;
    String shortDescribe;
    UUID sellerId;

    public SkillResponse(UUID id, String name, SkillLevel level, String shortDescribe, UUID sellerId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.shortDescribe = shortDescribe;
        this.sellerId = sellerId;
    }
}
