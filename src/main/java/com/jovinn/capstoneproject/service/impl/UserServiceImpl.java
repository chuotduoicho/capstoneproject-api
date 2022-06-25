package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.dto.request.SignUpRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ActivityTypeService;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.util.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityTypeService activityTypeService;
    private final EmailSender emailSender;

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
                currentUser.getLastName());
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} {} to the database",user.getFirstName(),user.getLastName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return new UserProfile(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getBirthDate(),
                user.getCity(), user.getCountry(), user.getAvatar());
    }

    @Override
    public User update(User editUser, UUID id, UserPrincipal currentUser) {
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user not found ", id));

        if (existUser.getId().equals(currentUser.getId())) {
            existUser.setFirstName(editUser.getFirstName());
            existUser.setLastName(editUser.getLastName());
            existUser.setPhoneNumber(editUser.getPhoneNumber());
            existUser.setGender(editUser.getGender());
            existUser.setBirthDate(editUser.getBirthDate());
            existUser.setCity(editUser.getCity());
            existUser.setCountry(editUser.getCountry());
            existUser.setAvatar(editUser.getAvatar());

            return userRepository.save(existUser);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + existUser.getUsername());
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setResetPasswordToken(null);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws ResourceNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email", "Email not present ", email));

        if(user != null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }else{
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

    @Override
    public User getByUserId(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Not found user by id", id));
    }

    @Override
    public ApiResponse registerUser(SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email đã được đăng kí");
        }
        String verificationCode = RandomString.make(15);
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUsername(signUpRequest.getUsername().toLowerCase());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setJoinedAt(new Date());
        user.setVerificationCode(verificationCode);
        user.setIsEnabled(Boolean.FALSE);
        user.setActivityType(activityTypeService.getByActivityType(UserActivityType.BUYER));
        String link = "http://localhost:3000/auth/verifyAccount/" + verificationCode;
        try {
            emailSender.sendEmailVerify(signUpRequest.getEmail(), link);
        } catch (UnsupportedEncodingException | MessagingException exception){
            return null;
        }
        userRepository.save(user);
        return new ApiResponse(Boolean.TRUE, "Liên kết xác thực đã được gửi vào hòm thư của bạn, vui lòng xác nhận");
    }

    @Override
    public User verifyRegistration(String verificationCode) throws ResourceNotFoundException {
        User user = userRepository.findUserByVerificationCode(verificationCode);
        if (user == null){
            throw new ResourceNotFoundException("User", "Verification code", verificationCode);
        }
        user.setIsEnabled(Boolean.TRUE);
        return userRepository.save(user);
    }
}
