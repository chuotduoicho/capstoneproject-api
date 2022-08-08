package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.dto.client.request.LoginRequest;
import com.jovinn.capstoneproject.dto.client.request.SignUpRequest;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.JwtTokenProvider;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.util.EmailSender;
import com.jovinn.capstoneproject.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EmailSender emailSender;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Tài khoản/email hoặc password không đúng");
        }
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
            String resetPasswordLink = RequestUtility.getSiteURL(request) + "/auth/resetpassword/" + token;
            emailSender.sendEmailResetPassword(email, resetPasswordLink);
        } catch (ResourceNotFoundException ex) {
            return "User not found with email: " + email;
        } catch (UnsupportedEncodingException | MessagingException e) {
            return "Error while sending email";
        }
        return token;
    }
}
