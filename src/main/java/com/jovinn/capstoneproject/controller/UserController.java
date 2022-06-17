package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    //Get list all user - Using for admin
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        return ResponseEntity.created(uri).body(userService.getUsers());
    }

    //View buyer infor throught user - Using for seller
//    @GetMapping("/buyer/{id}")
//    public User getBuyer(@PathVariable UUID id) {
//        return userService.findByUserId(id);
//    }
    @GetMapping("/list")
    public List<User> getListUsers() {
        return userService.getUsers();
    }
    //View seller infor throught user - Using for buyer
    @GetMapping("/seller/{id}")
    public User findSellerById(@PathVariable UUID id) {
        return null;
    }

    //Update user can using function like seller - Using for buyer (One time)
    @PostMapping("/joinSelling")
    public User joinSelling(@RequestBody User user) {
        return null;
    }

    //Edit profile for User - Using for buyer and seller
//    @PostMapping("/edit/{id}")
//    public User updateUser(@PathVariable UUID id) {
//        User existUser = userService.findByUserId(id);
//        return userService.saveUser(existUser);
//    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(currentUser);

        return new ResponseEntity< >(userSummary, HttpStatus.OK);
    }
}
