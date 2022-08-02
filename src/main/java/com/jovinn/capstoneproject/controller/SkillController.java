package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataRequest;
import com.jovinn.capstoneproject.dto.adminsite.SkillMetaDataResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.service.SkillMetaDataService;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skill")
@CrossOrigin(origins = "*")
public class SkillController {
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillMetaDataService skillMetaDataService;

    @GetMapping("/getAllSkillBySellerId/{id}")
    public List<Skill> getAllSkillBySellerId(@PathVariable UUID id){
        return skillService.getAllSkillBySellerId(id);
    }

    @GetMapping("/meta-data/{subCategoryId}")
    public ResponseEntity<List<SkillMetaDataResponse>> getSkillMetaBySubCategory(@PathVariable("subCategoryId") UUID subCategoryId) {
        List<SkillMetaDataResponse> responses = skillMetaDataService.getSkillMetaBySubCategoryId(subCategoryId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/meta-data-list")
    public ResponseEntity<List<SkillMetaDataResponse>> getSkillMetaDataList() {
        List<SkillMetaDataResponse> responses = skillMetaDataService.getSkillMetaDataList();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/meta-data/{id}")
    public ResponseEntity<SkillMetaDataResponse> getSkillMetaData(@PathVariable("id") UUID id) {
        SkillMetaDataResponse response = skillMetaDataService.getSkillMeta(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/meta-data")
    public ResponseEntity<SkillMetaDataResponse> addSkillMetaByAdmin(@Valid @RequestBody SkillMetaDataRequest request) {
        SkillMetaDataResponse response = skillMetaDataService.addSkillMetaData(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/meta-data/{id}")
    public ResponseEntity<ApiResponse> updateSkillMetaByAdmin(@PathVariable("id") UUID id,
                                                              @Valid @RequestBody SkillMetaDataRequest request) {
        ApiResponse response = skillMetaDataService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meta-data/{id}")
    public ResponseEntity<ApiResponse> updateSkillMetaByAdmin(@PathVariable("id") UUID id) {
        ApiResponse response = skillMetaDataService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
