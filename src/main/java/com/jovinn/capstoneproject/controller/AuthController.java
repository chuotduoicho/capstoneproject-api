package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.JwtAuthenticationResponse;
import com.jovinn.capstoneproject.dto.request.LoginRequest;
import com.jovinn.capstoneproject.dto.request.SignUpRequest;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.JwtTokenProvider;
import com.jovinn.capstoneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.print.DocFlavor;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
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

    @Autowired
    private UserService userService;

    private final JavaMailSender mailSender;

//    @PostMapping("/signin")
//    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtTokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
//    }
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt =jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Username/Email or password does not exist");
        }
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

        String verificationCode = RandomString.make(15);

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
        user.setVerificationCode(verificationCode);
        user.setEnabled(false);
        User result = userRepository.save(user);
        String link = "http://localhost:3000/auth/verifyAccount/" + verificationCode;
        try{
            sendEmail(signUpRequest.getEmail(), link);
        }catch (UnsupportedEncodingException | MessagingException exception){
            return null;
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Liên kết xác thực đã được gửi vào email của bạn!"));
//        return result;
    }

    @PutMapping("/verify/{verificationCode}")
    public ResponseEntity<ApiResponse> verifyAccount(@PathVariable String verificationCode ){
        User user = userService.verifyRegistration(verificationCode);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Đăng ký tài khoản thành công"));
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to verify your email";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to register.</p>"
                + "<p>Click the link below to verify your email:</p>"
                + "<p><a href=\"" + link + "\">Verify my email</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do have an account with this email, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
