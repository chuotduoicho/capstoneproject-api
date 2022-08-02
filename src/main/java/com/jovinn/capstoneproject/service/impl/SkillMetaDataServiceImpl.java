package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataRequest;
import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.exception.BadRequestException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.model.SkillMetaData;
import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.repository.SkillMetaDataRepository;
import com.jovinn.capstoneproject.repository.SubCategoryRepository;
import com.jovinn.capstoneproject.service.SkillMetaDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillMetaDataServiceImpl implements SkillMetaDataService {
    @Autowired
    private SkillMetaDataRepository skillMetaDataRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SkillMetaDataResponse> getSkillMetaBySubCategoryId(UUID subCategoryId) {
        try {
            List<SkillMetaData> skillMetaDataList = skillMetaDataRepository.findAllBySubCategoryId(subCategoryId);
            return skillMetaDataList.stream()
                    .map(skillMetaData -> modelMapper.map(skillMetaData, SkillMetaDataResponse.class))
                    .collect(Collectors.toList());
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã xảy ra lỗi");
        }
    }

    @Override
    public SkillMetaDataResponse getSkillMeta(UUID metaId) {
        SkillMetaData skillMetaData = skillMetaDataRepository.findById(metaId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy kỹ năng " + metaId));
        return new SkillMetaDataResponse(skillMetaData.getId(), skillMetaData.getSubCategory().getId(), skillMetaData.getName());
    }

    @Override
    public List<SkillMetaDataResponse> getSkillMetaDataList() {
        try {
            List<SkillMetaData> skillMetaDataList = skillMetaDataRepository.findAll();
            return skillMetaDataList.stream()
                    .map(skillMetaData -> modelMapper.map(skillMetaData, SkillMetaDataResponse.class))
                    .collect(Collectors.toList());
        } catch(BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã xảy ra lỗi");
        }
    }

    @Override
    public SkillMetaDataResponse addSkillMetaData(SkillMetaDataRequest request) {
        try {
            SkillMetaData skillMetaData = new SkillMetaData();
            SubCategory subCategory = subCategoryRepository.findSubCategoryById(request.getSubCategoryId());
            skillMetaData.setName(request.getName());
            skillMetaData.setSubCategory(subCategory);
            SkillMetaData save = skillMetaDataRepository.save(skillMetaData);
            return new SkillMetaDataResponse(save.getId(), save.getSubCategory().getId(), save.getName());
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã có lỗi khi tạo kỹ năng");
        }
    }

    @Override
    public ApiResponse update(UUID metaId, SkillMetaDataRequest request) {
        try {
            SkillMetaData skillMetaData = skillMetaDataRepository.findById(metaId)
                    .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy kỹ năng này"));
            SubCategory subCategory = subCategoryRepository.findSubCategoryById(request.getSubCategoryId());
            skillMetaData.setName(request.getName());
            skillMetaData.setSubCategory(subCategory);
            return new ApiResponse(Boolean.TRUE, "Cập nhật meta skill data thành công");
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã có lỗi khi cập nhật kỹ năng");
        }
    }

    @Override
    public ApiResponse delete(UUID metaId) {
        try {
            SkillMetaData skillMetaData = skillMetaDataRepository.findById(metaId)
                    .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy kỹ năng này"));
            skillMetaDataRepository.deleteById(skillMetaData.getId());
            return new ApiResponse(Boolean.TRUE, "Xóa thành công");
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã có lỗi khi xóa");
        }
    }
}
