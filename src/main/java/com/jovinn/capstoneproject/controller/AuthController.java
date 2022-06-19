package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.dto.request.LoginRequest;
import com.jovinn.capstoneproject.dto.request.SignUpRequest;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
//        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
//        }
//
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
//        }
//
//        User user = new User();
//        user.setFirstName(signUpRequest.getFirstName());
//        user.setLastName(signUpRequest.getLastName());
//        user.setUsername(signUpRequest.getUsername());
//        user.setEmail(signUpRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//        user.setJoinedAt(new Date());
//
//        ActivityType roles = new ActivityType();
//        roles = activityTypeRepository.findByActivityType(UserActivityType.BUYER).get();
//
//        user.setActivityType(Collections.singleton(roles));
//
//        User result = userRepository.save(user);
////        ActivityType at = activityTypeRepository.findByActivityType(UserActivityType.BUYER).get();
////        at.setActivityType(UserActivityType.BUYER);
////        user.setActivityType(Collections.singleton(at));
//
//        Buyer buyer = new Buyer();
//        buyer.setBuyerNumber(getRandomNumberString());
//        user.setBuyer(buyer);
//
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
//    }
//

//    @PostMapping("/register")   //api method post : url :'http://localhost:8080/api/auth/register'
//    public ResponseEntity<User> register(@RequestBody User user){
//        return ResponseEntity.ok().body(userService.saveUser(user));
//    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtTokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
//    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email đã được đăng kí");
        }


        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUsername(signUpRequest.getUsername().toLowerCase());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        ActivityType at = activityTypeRepository.findByActivityType(UserActivityType.BUYER).get();
        at.setActivityType(UserActivityType.BUYER);
        user.setActivityType(Collections.singleton(at));
        Buyer buyer = new Buyer();
        buyer.setBuyerNumber(getRandomNumberString());
        buyer.setUser(user);
        user.setBuyer(buyer);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Đăng ký tài khoản thành công"));
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
