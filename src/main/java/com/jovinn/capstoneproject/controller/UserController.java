package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@CrossOrigin(origins = "**")
public class UserController {
    @Autowired
    private UserService userService;

    //Get list all user - Using for admin
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers(){
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
//        return ResponseEntity.created(uri).body(userService.getUsers());
//    }

    //Update profile user - For both role
    @PutMapping("/profile/edit/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
        userService.updateUser(id, user);
        return new ResponseEntity<>(userService.getByUserId(id), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public User getUserProfile(@PathVariable UUID id) {
        return userService.getByUserId(id);
    }
//    @PutMapping("/me/profile/{id}")
//    public ResponseEntity<User> updateUserProfile(@PathVariable UUID id, @RequestBody User user) {
//        User existUser = userService.getByUserId(id);
//        existUser.setFirstName(user.getFirstName());
//        existUser.setLastName(user.getLastName());
//        existUser.setPhoneNumber(user.getPhoneNumber());
//        existUser.setGender(user.getGender());
//        existUser.setBirthDate(user.getBirthDate());
//        existUser.setAddress(user.getAddress());
//        existUser.setProvince(user.getProvince());
//        existUser.setCity(user.getCity());
//        existUser.setCountry(user.getCountry());
//        existUser.setAvatar(user.getAvatar());
//
//        User update = userService.saveUser(existUser);
//        return ResponseEntity.ok().body(update);
//    }
    //View buyer infor throught user - Using for seller
//    @GetMapping("/buyer/{id}")
//    public User getBuyer(@PathVariable UUID id) {
//        return userService.findByUserId(id);
//    }
    @GetMapping("/users")
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
//    @GetMapping("/me")
//    @PreAuthorize("hasRole('BUYER')")
//    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
//        UserSummary userSummary = userService.getCurrentUser(currentUser);
//
//        return new ResponseEntity< >(userSummary, HttpStatus.OK);
//    }
}
