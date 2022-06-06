package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User saveUser(User user);
    User getUser(String username);
    List<User> getUsers();
}
