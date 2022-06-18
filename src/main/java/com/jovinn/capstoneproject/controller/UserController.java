package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.ResetPasswordRequest;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@CrossOrigin(origins = "**")
public class UserController {
    @Autowired
    private UserService userService;
    //@Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;

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
            String resetPasswordLink = RequestUtility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
        } catch (ResourceNotFoundException ex) {
            return "User not found with email: "+email;
        } catch (UnsupportedEncodingException | MessagingException e) {
            return "Error while sending email";
        }
        return token;
    }
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ducdmhe141516@fpt.edu.vn", "Jovinn support");
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
    public String processResetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String password = request.getPassword();
        User user = userService.getUserByResetPasswordToken(token);
        if(user == null){
            return "Invalid token: "+token;
        }
        else{
            userService.updatePassword(user, password);
            return "You have succcessfully changed your password.";
        }
    }
}
