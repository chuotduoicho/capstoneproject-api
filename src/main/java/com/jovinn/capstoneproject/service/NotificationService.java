package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.NotificationResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface NotificationService {
    ApiResponse readNotification(UUID notificationId, UserPrincipal currentUser);
    NotificationResponse getNotifications(UserPrincipal currentUser);

    ApiResponse deleteNotification(UUID notificationId, UserPrincipal currentUser);
}
