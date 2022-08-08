package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.model.Notification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    List<Notification> list;
    Integer unread;

    public NotificationResponse(List<Notification> list, Integer unread) {
        this.list = list;
        this.unread = unread;
    }
}
