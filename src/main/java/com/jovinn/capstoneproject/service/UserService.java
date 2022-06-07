package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.ApiResponse;
import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.model.User;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);
    UserProfile getUserProfile(String username);
    UserProfile getUserProfileById(UUID id);
    User saveUser(User user);
    User getUser(String username);
    List<User> getUsers();
    User getUserById(UUID id);
    String deleteCategoryById(UUID id);
    User updateUser(User user);
    User updateUser(User newUser, String username, UserPrincipal currentUser);
    ApiResponse deleteUser(String username, UserPrincipal currentUser);

    UserProfile updateProfile(UserProfile userProfile);
}
