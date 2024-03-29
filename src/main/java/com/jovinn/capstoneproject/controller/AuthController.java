package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.dto.client.request.LoginRequest;
import com.jovinn.capstoneproject.dto.client.request.SignUpRequest;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.JwtTokenProvider;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.util.EmailSender;
import com.jovinn.capstoneproject.util.WebConstant;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSender emailSender;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        ApiResponse apiResponse = userService.registerUser(signUpRequest);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/verify/{verificationCode}")
    public ResponseEntity<ApiResponse> verifyAccount(@PathVariable String verificationCode ){
        User user = userService.verifyRegistration(verificationCode);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Đăng ký tài khoản thành công"));
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(10);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = WebConstant.DOMAIN + "/auth/resetPassword/" + token;
            emailSender.sendEmailResetPassword(email, resetPasswordLink);
        } catch (ResourceNotFoundException ex) {
            return "Không tìm thấy Email: " + email;
        } catch (UnsupportedEncodingException | MessagingException e) {
            return "Có lỗi khi gửi Email";
        }
        return token;
    }
}
