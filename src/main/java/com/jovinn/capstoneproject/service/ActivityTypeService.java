package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;

import java.util.Set;

public interface ActivityTypeService {
    Set<ActivityType> getByActivityType(UserActivityType type);
    ActivityType saveType(ActivityType type);
}
