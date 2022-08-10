package com.jovinn.capstoneproject.repository.auth;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, UUID> {
    Set<ActivityType> findByActivityType(UserActivityType type);

    ActivityType findByUsers_Id(UUID id);
}
