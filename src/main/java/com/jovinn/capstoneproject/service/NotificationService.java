package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerTargetPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.NotificationResponse;
import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificationService {
    ApiResponse readNotification(UUID notificationId, UserPrincipal currentUser);
    NotificationResponse getNotifications(UserPrincipal currentUser);
    SseEmitter registerClient();
    Notification sendEventToClients(Notification notification);

    Notification inviteUser(ListSellerTargetPostRequestResponse targetSeller);

    ApiResponse deleteNotification(UUID notificationId, UserPrincipal currentUser);


}
