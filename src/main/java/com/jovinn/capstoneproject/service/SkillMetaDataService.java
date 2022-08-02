package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataRequest;
import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface SkillMetaDataService {
    List<SkillMetaDataResponse> getSkillMetaBySubCategoryId(UUID subCategoryId);
    SkillMetaDataResponse getSkillMeta(UUID metaId);
    List<SkillMetaDataResponse> getSkillMetaDataList();
    SkillMetaDataResponse addSkillMetaData(SkillMetaDataRequest request);
    ApiResponse update(UUID metaId, SkillMetaDataRequest request);

    ApiResponse delete(UUID metaId);
}
