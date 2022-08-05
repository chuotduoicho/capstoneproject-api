package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerTargetPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.NotificationResponse;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.NotificationRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

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
        List<Notification> readList = notificationRepository.findAllByUnread(Boolean.FALSE);
        Integer countUnread = notifications.size() - readList.size();
        return new NotificationResponse(notifications, countUnread);
    }

    @Override
    public SseEmitter registerClient() {
        SseEmitter emitter = new SseEmitter();

        // Add client specific new emitter in global list
        emitters.add(emitter);

        // On Client connection completion, unregister client specific emitter
        emitter.onCompletion(() -> emitters.remove(emitter));

        // On Client connection timeout, unregister and mark complete client specific emitter
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
        return emitter;
    }

    @Override
    public Notification sendEventToClients(Notification notification) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for(SseEmitter emitter : emitters){
            try {
                emitter.send("reload command");
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        return notificationRepository.save(notification);
    }

    public void sendEvent(){
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for(SseEmitter emitter : emitters){
            try {
                emitter.send("reload command");
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
    }
    @Override
    public Notification inviteUser(ListSellerTargetPostRequestResponse targetSeller){
        User user = userRepository.findUserById(targetSeller.getUserId());
        Notification invitation = new Notification();
        invitation.setUser(user);
        invitation.setLink("");
        invitation.setShortContent("Bạn nhận được lời mời từ "+ user.getUsername());
        sendEvent();
        return notificationRepository.save(invitation);
    }
}
