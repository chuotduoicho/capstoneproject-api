package com.jovinn.capstoneproject.util;

import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PushNotification {
    @Autowired
    private NotificationRepository repository;

    public void sendNotification(User user, String link, String shortContent) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setLink(link);
        notification.setShortContent(shortContent);
        notification.setUnread(Boolean.TRUE);
        repository.save(notification);
    }
}
