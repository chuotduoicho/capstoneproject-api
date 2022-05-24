package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.domain.Role;
import com.jovinn.capstoneproject.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();

}
