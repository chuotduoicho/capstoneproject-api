package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);
    User saveUser(User user);
    List<User> getUsers();
    UserProfile getUserProfile(String username);

    User getUserByEmail(String email);
    User getUserByResetPasswordToken(String token);
    void updatePassword(User user, String newPassword);
    void updateResetPasswordToken(String token, String email);

//    void updateUser(UUID id, User user);
    User update(User newUser, UUID id, UserPrincipal currentUser);

    User getByUserId(UUID id);

    User verifyRegistration(String verificationCode);




}
