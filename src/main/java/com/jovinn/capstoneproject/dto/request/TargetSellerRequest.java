package com.jovinn.capstoneproject.dto.request;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TargetSellerRequest {
    UUID subCategoryId;
    RankSeller rankSeller;
    Set<String> skillName;
}
