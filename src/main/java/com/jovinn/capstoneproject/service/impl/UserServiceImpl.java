package com.jovinn.capstoneproject.service.impl;

//import com.jovinn.capstoneproject.model.Role;
import com.jovinn.capstoneproject.dto.ApiResponse;
import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
//import com.jovinn.capstoneproject.repo.RoleRepo;
//import com.jovinn.capstoneproject.repository.UserProfileRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService , UserDetailsService {

    private final UserRepository userRepository;

    //private final UserProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else{
            log.info("User found in the database: {} role {}",username,user.getActivityType().toString());
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserActivityType.BUYER.toString()));
        authorities.add(new SimpleGrantedAuthority(UserActivityType.SELLER.toString()));
//        user.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return null;
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.getUserByName(username);
        return new UserProfile(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPhone_number(), user.getGender(),
                user.getBirth_date(), user.getAddress(),
                user.getProvince(), user.getCity(),
                user.getCountry(), user.getAvatar());
    }

    @Override
    public UserProfile getUserProfileById(UUID id) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} {} to the database",user.getFirstName(),user.getLastName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    @Override
//    public Role saveRole(Role role) {
//        log.info("Saving new role {} to the database", role.getName());
//        return roleRepo.save(role);
//    }
//
//    @Override
//    public void addRoleToUser(String username, String roleName) {
//        log.info("Adding role {} to user {}", roleName, username);
//        User user = userRepo.findByUsername(username);
//        Role role= roleRepo.findByName(roleName);
//        user.getRoles().add(role);
//    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");

        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteCategoryById(UUID id) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        return null;
    }

//    @Override
//    public UserProfile updateProfile(UserProfile userProfile) {
//        UserProfile existUser = profileRepository.findById(userProfile.getId()).orElse(null);
//        existUser.setFirstName(userProfile.getFirstName());
//        existUser.setLastName(userProfile.getLastName());
//        existUser.setPhoneNumber(userProfile.getPhoneNumber());
//        existUser.setGender(userProfile.getGender());
//        existUser.setBirthDate(userProfile.getBirthDate());
//        existUser.setAddress(userProfile.getAddress());
//        existUser.setProvince(userProfile.getProvince());
//        existUser.setCity(userProfile.getCity());
//        existUser.setCountry(userProfile.getCountry());
//        return profileRepository.save(existUser);
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
    public String updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setResetPasswordToken(null);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return encodedPassword;
    }

    @Override
    public String updateResetPasswordToken(String token, String email) throws ResourceNotFoundException {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user!=null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }else{
            throw new ResourceNotFoundException("User","email",email);
        }
        return token;
    }


}
