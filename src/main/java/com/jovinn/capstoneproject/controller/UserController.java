package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.dto.request.ResetPasswordRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
//@CrossOrigin(origins = "**")
public class UserController {
    @Autowired
    private UserService userService;
    //@Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(currentUser);

        return new ResponseEntity< >(userSummary, HttpStatus.OK);
    }


//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers() {
//
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
//        return ResponseEntity.created(uri).body(userService.getUsers());
//    }


//    @GetMapping("/user/{id}")
//    public User getUserById(@PathVariable UUID id) {
//        return userService.getUserById(id);
//    }

    //Update profile user - For both role
//    @PutMapping("/profile/edit/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
//        userService.u(id, user);
//        return new ResponseEntity<>(userService.getByUserId(id), HttpStatus.OK);
//    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser,
                                           @PathVariable("id") UUID id, @CurrentUser UserPrincipal currentUser) {
        User updatedUSer = userService.update(newUser, id, currentUser);

        return new ResponseEntity< >(updatedUSer, HttpStatus.CREATED);
    }


    //    @PutMapping("/update/{username}")
//    public ResponseEntity<UserDetails> updateUser(@RequestParam("username") String username, @RequestBody User user) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
//        String name = userDetails.getUsername();
//        userService.update(username, user);
//        return new ResponseEntity<>(customUserDetailsService.loadUserByUsername(name), HttpStatus.OK);
//    }
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
//
//    @GetMapping("user/forgot_password")
//    public String forgotPasswordForm(){
//        return "forgot_password_form";
//    }
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(10);
        try{
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://localhost:3000/auth/resetPassword/" + token;
            sendEmail(email, resetPasswordLink);
        } catch (ResourceNotFoundException ex) {
            return "User not found with email: " + email;
        } catch (UnsupportedEncodingException | MessagingException e) {
            return "Error while sending email";
        }
        return token;
    }
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
    @PostMapping("/reset_password")
    public ResponseEntity<ApiResponse> processResetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String password = request.getPassword();
        User user = userService.getUserByResetPasswordToken(token);
        userService.updatePassword(user, password);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Đổi mật khẩu thành công"));

    }
}
