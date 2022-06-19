package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                user.getAddress(), user.getProvince(), user.getCity(), user.getCountry(), user.getAvatar());
    }

//    @Override
//    public void updateUser(UUID id, User user) {
//        User existUser = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User", "Not found user by ", id));
//
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
//        userRepository.save(existUser);
//    }

    @Override
    public User update(User newUser, UUID id, UserPrincipal currentUser) {
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user not found ", id));

        if (existUser.getId().equals(currentUser.getId())) {
            existUser.setFirstName(newUser.getFirstName());
            existUser.setLastName(newUser.getLastName());
            existUser.setPhoneNumber(newUser.getPhoneNumber());
            existUser.setGender(newUser.getGender());
            existUser.setBirthDate(newUser.getBirthDate());
            existUser.setAddress(newUser.getAddress());
            existUser.setProvince(newUser.getProvince());
            existUser.setCity(newUser.getCity());
            existUser.setCountry(newUser.getCountry());
            existUser.setAvatar(newUser.getAvatar());

            return userRepository.save(existUser);

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + existUser.getUsername());
        throw new UnauthorizedException(apiResponse);
    }

//    @Override
//    public void update(String username, User user) {
//        User existUser = userRepository.findByUsername(username)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User", "Not found user by ", username));
//
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
//        userRepository.save(existUser);
//    }

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
}
