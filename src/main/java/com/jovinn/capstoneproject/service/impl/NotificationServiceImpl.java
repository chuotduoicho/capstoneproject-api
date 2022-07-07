package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.repository.NotificationRepository;
import com.jovinn.capstoneproject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
