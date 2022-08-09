package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ActivityTypeServiceImplTest {
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @InjectMocks
    private ActivityTypeServiceImpl activityTypeService;
    private ActivityType newActivityType;
    @BeforeEach
    public void setup(){
        newActivityType = ActivityType.builder()
                .id(1l)
                .activityType(UserActivityType.SELLER)
                .build();
    }
    @DisplayName("JUnit test for getActivityTypeByUserActivityType method")
    @Test
    void givenSetOfUserActivityType_whenGetActivityTypeByUserActivityType_thenReturnSetOfActivityType() {
        // given - precondition or setup
        ActivityType activityType1 = ActivityType.builder()
                .id(2l)
                .activityType(UserActivityType.SELLER)
                .build();
        given(activityTypeRepository.findByActivityType(UserActivityType.SELLER)).willReturn(Set.of(newActivityType,activityType1));
        // when -  action or the behaviour that we are going test
        Set<ActivityType> activityTypes = activityTypeService.getByActivityType(UserActivityType.SELLER);
        // then - verify the output
        Assertions.assertThat(activityTypes).isNotNull();
        Assertions.assertThat(activityTypes.size()).isEqualTo(2);
    }
    @DisplayName("JUnit test for saveActivity method")
    @Test
    void givenActivityTypeObject_whenSaveActivityType_thenReturnActivityTypeObject() {
        // given - precondition or setup
        given(activityTypeRepository.save(newActivityType)).willReturn(newActivityType);
        // when -  action or the behaviour that we are going test
        ActivityType activityType = activityTypeService.saveType(newActivityType);
        // then - verify the output
        Assertions.assertThat(activityType).isEqualTo(newActivityType);
        Assertions.assertThat(activityType).isNotNull();
    }
}