package com.jovinn.capstoneproject.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public interface CustomerUserDetailService {
    UserDetails loadUserByUserName(String userNameOrEmail) throws UsernameNotFoundException;
    UserDetails loadUserById(UUID id);
}
