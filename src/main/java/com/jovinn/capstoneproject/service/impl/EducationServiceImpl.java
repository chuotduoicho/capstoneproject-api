package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Education;
import com.jovinn.capstoneproject.repository.EducationRepository;
import com.jovinn.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationRepository educationRepository;

    @Override
    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }
}
