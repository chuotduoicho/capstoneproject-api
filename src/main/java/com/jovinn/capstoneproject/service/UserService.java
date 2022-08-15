package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.dto.adminsite.adminrequest.AdminLoginRequest;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminViewUserResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountUserResponse;
import com.jovinn.capstoneproject.dto.client.request.*;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);
    User saveUser(User user);
    List<User> getUsers();
    UserProfile getUserProfile(String username);
    User getUserByEmail(String email);
    ApiResponse resetPassword(ResetPasswordRequest request);
    void updatePassword(User user, String newPassword);
    void updateResetPasswordToken(String token, String email);
    ApiResponse update(UUID id, UserChangeProfileRequest request, UserPrincipal currentUser);
    User getByUserId(UUID id);
    ApiResponse registerUser(SignUpRequest signUpRequest);
    JwtAuthenticationResponse loginUser(LoginRequest loginRequest);
    JwtAuthenticationResponse loginAdmin(AdminLoginRequest adminLoginRequest);
    User verifyRegistration(String verificationCode);
    ApiResponse changePassword(ChangePasswordRequest request, UserPrincipal currentUser);
    List<UserProfile> getListUserInvitedByPostRequestId(UUID postRequest);
    CountUserResponse countUserById();
    ApiResponse banOrUnbanUser(UUID userId);
    AdminViewUserResponse getUserById(UUID id);
    User getUserByUserName(String name);
}
