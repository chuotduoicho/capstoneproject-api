package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.DeliveryRequest;
import com.jovinn.capstoneproject.dto.response.DeliveryResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface DeliveryService {
    DeliveryResponse updateDelivery(UUID id, DeliveryRequest request, UserPrincipal currentUser);
}
