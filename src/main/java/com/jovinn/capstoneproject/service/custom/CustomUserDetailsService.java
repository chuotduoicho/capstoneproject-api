package com.jovinn.capstoneproject.service.custom;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return null;
//        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with username or email" + usernameOrEmail));
//        return new org.springframework.security.core.userdetails.User(user.getEmail(),
//                user.getPassword(), mapRolesToAuthorities(user.getActivityType()));
//        return null;
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
//        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with username or email" + usernameOrEmail));
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<ActivityType> activityType){
        return activityType.stream().map(role -> new SimpleGrantedAuthority(role.getActivityType().name())).collect(Collectors.toList());
    }

    //    UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;
//
//    UserDetails loadUserById(UUID id) {
//        return null;
//    }

}