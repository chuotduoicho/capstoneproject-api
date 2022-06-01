//package com.jovinn.capstoneproject.service.impl;
//
//import com.jovinn.capstoneproject.model.Role;
//import com.jovinn.capstoneproject.model.User;
//import com.jovinn.capstoneproject.repository.RoleRepo;
//import com.jovinn.capstoneproject.repository.UserRepository;
//import com.jovinn.capstoneproject.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//@Slf4j
//public class UserServiceImpl implements UserService , UserDetailsService {
//
//    private final UserRepository userRepo;
//    private final RoleRepo roleRepo;
//    private final PasswordEncoder passwordEncoder;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByUsername(username);
//        if(user == null){
//            log.error("User not found in the database");
//            throw new UsernameNotFoundException("User not found in the database");
//        }else{
//            log.info("User found in the database: {}",username);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        user.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
//    }
//    @Override
//    public User saveUser(User user) {
//        log.info("Saving new user {} to the database",user.getLast_name());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepo.save(user);
//    }
//
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
//
//    @Override
//    public User getUser(String username) {
//        log.info("Fetching user {}", username);
//        return userRepo.findByUsername(username);
//    }
//
//    @Override
//    public List<User> getUsers() {
//        log.info("Fetching all users");
//        return userRepo.findAll();
//    }
//
//    @Override
//    public User getUserById(UUID id) {
//        return userRepo.findById(id).orElse(null);
//    }
//
//    @Override
//    public User updateUser(User user) {
//        User existingUser = userRepo.findById(user.getId()).orElse(null);
//
//        existingUser.setFirst_name(user.getFirst_name());
//        existingUser.setLast_name(user.getLast_name());
//        existingUser.setEmail(user.getEmail());
//        existingUser.setPhone_number(user.getPhone_number());
//        existingUser.setGender(user.getGender());
//        existingUser.setBirth_date(user.getBirth_date());
//
//        return userRepo.save(existingUser);
//    }
//
//}
