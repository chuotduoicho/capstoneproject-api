package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.NotificationResponse;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.repository.NotificationRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public ApiResponse readNotification(UUID notificationId, UserPrincipal currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy thông báo"));
        if(notification.getUser().getId().equals(currentUser.getId())) {
            notification.setUnread(Boolean.FALSE);
            notificationRepository.save(notification);
            return new ApiResponse(Boolean.TRUE, "" + notification.getLink());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public NotificationResponse getNotifications(UserPrincipal currentUser) {
        List<Notification> notifications = notificationRepository.findAllByUserId(currentUser.getId());
        List<Notification> readList = notificationRepository.findAllByUnreadAndUserId(Boolean.FALSE, currentUser.getId());
        Integer countUnread = notifications.size() - readList.size();
        return new NotificationResponse(notifications, countUnread);
    }

    @Override
    public ApiResponse deleteNitification(UUID notificationId, UserPrincipal currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy thông báo"));
        notificationRepository.delete(notification);
        return new ApiResponse(Boolean.TRUE, "" + notification.getId());
    }
}
