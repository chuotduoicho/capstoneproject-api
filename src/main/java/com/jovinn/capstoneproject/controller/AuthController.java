package com.jovinn.capstoneproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.payload.ApiResponse;
import com.jovinn.capstoneproject.payload.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.payload.request.LoginRequest;
import com.jovinn.capstoneproject.payload.request.SignUpRequest;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.security.JwtTokenProvider;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/register")   //api method post : url :'http://localhost:8080/api/auth/register'
    public ResponseEntity<User> register(@RequestBody User user){
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.ok().body(userService.saveUser(user));
    }
//    @GetMapping("/auth/token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//                String email = decodedJWT.getSubject();
//                User user = userService.getUser(email);
//                String access_token = JWT.create()
//                        .withSubject(user.getEmail())
//                        .withExpiresAt(new Date(System.currentTimeMillis() +10*60*1000))
//                        .withIssuer(request.getRequestURL().toString())
//                        .withClaim("roles", user.getActivityType().toString())
//                        .sign(algorithm);
//                Map<String,String> tokens = new HashMap<>();
//                tokens.put("access_token",access_token);
//                tokens.put("refresh_token",refresh_token);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
//            }catch (Exception exception){
//
//                response.setHeader("error",exception.getMessage());
//                response.setStatus(FORBIDDEN.value());
////                      response.sendError(FORBIDDEN.value());
//                Map<String,String> error = new HashMap<>();
////                      tokens.put("access_token",access_token);
//                error.put("error_message",exception.getMessage());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(),error);
//            }
//
//        }else{
//            throw new  RuntimeException("Refresh token is missing");
//        }
//    }
@PostMapping("/signin")
public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
}

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email đã được đăng kí");
        }

        String firstName = signUpRequest.getFirstName().toLowerCase();

        String lastName = signUpRequest.getLastName().toLowerCase();

        String username = signUpRequest.getUsername().toLowerCase();

        String email = signUpRequest.getEmail().toLowerCase();

        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
    }
}
