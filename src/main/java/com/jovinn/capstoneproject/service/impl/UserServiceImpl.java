package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.UserSummary;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void updateUser(UUID id, User user) {
        User existUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Not found user by ", id));

        existUser.setFirstName(user.getFirstName());
        existUser.setLastName(user.getLastName());
        existUser.setPhoneNumber(user.getPhoneNumber());
        existUser.setGender(user.getGender());
        existUser.setBirthDate(user.getBirthDate());
        existUser.setAddress(user.getAddress());
        existUser.setProvince(user.getProvince());
        existUser.setCity(user.getCity());
        existUser.setCountry(user.getCountry());
        existUser.setAvatar(user.getAvatar());

        userRepository.save(existUser);
    }

    @Override
    public User getByUserId(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Not found user by id", id));
    }

//    @Override
//    public User findByUserId(UUID id) {
//        return userRepository.findByUserId(id);
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
}
