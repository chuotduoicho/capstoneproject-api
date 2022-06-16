package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);
    User saveUser(User user);
//    User getUser(String username);
    List<User> getUsers();
}
