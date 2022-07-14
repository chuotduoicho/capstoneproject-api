package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.DeliveryHaveMilestoneRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryNotMilestoneRequest;
import com.jovinn.capstoneproject.dto.response.DeliveryHaveMilestoneResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryNotMilestoneResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface DeliveryService {
    DeliveryNotMilestoneResponse createDelivery(UUID contractId, DeliveryNotMilestoneRequest request, UserPrincipal currentUser);
    DeliveryHaveMilestoneResponse createDeliveryMilestone(UUID contractId, DeliveryHaveMilestoneRequest request, UserPrincipal currentUser);
    DeliveryNotMilestoneResponse update(UUID deliveryId, DeliveryNotMilestoneRequest request, UserPrincipal currentUser);
}
