package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Override
    public Set<ActivityType> getByActivityType(UserActivityType type) {
        return activityTypeRepository.findByActivityType(type);
    }

    @Override
    public ActivityType saveType(ActivityType type) {
        return activityTypeRepository.save(type);
    }

    @Override
    public UserActivityType getActivityTypeByUserId(UUID id) {
        ActivityType activityType = activityTypeRepository.findByUsers_Id(id);
        return activityType.getActivityType();
    }
}
