package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Role;
import com.jovinn.capstoneproject.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface UserService {
//    User saveUser(User user);
//    Role saveRole(Role role);
//    void addRoleToUser(String username, String roleName);
//    User getUser(String username);
//    List<User> getUsers();
//    User getUserById(UUID id);
//    User updateUser(User user);
    String signUpUser(User user);
    String register(User user);
}
