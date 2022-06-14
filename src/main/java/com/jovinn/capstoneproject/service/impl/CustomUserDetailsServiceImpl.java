package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

public class CustomUserDetailsServiceImpl  {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) {
//        User user = userRepository.findByUsernameOrEmail(username, username)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with this username or email: %s", username)));
//        return UserPrincipal.create(user);
//    }

//    @Override
//    public UserDetails loadUserById(UUID id) {
//        return null;
//    }

//    @Override
//    @Transactional
//    public UserDetails loadUserById(UUID id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with id: %s", id)));
//
//        return UserPrincipal.create(user);
//    }
}
