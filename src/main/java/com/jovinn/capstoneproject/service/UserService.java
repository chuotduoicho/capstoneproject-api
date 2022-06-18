package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);
    User saveUser(User user);
    List<User> getUsers();

    User getUserByEmail(String email);
    User getUserByResetPasswordToken(String token);
    String updatePassword(User user, String newPassword);
    String updateResetPasswordToken(String token, String email);

    void updateUser(UUID id, User user);
    User getByUserId(UUID id);
    //User findByUserId(UUID id);
}
