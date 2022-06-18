package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Override
    public ActivityType saveType(ActivityType type) {
        return activityTypeRepository.save(type);
    }
}
