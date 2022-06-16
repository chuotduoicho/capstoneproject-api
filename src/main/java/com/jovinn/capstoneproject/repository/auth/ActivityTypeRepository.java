package com.jovinn.capstoneproject.repository.auth;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, UUID> {
    Optional<ActivityType> findByActivityType(UserActivityType activityType);
}
