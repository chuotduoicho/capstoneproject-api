package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    //@Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers() {
//
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
//        return ResponseEntity.created(uri).body(userService.getUsers());
//    }

    @GetMapping("/user/{username}")
    public UserProfile getUserByUsername(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getUsers();
    }

//    @GetMapping("/user/{id}")
//    public User getUserById(@PathVariable UUID id) {
//        return userService.getUserById(id);
//    }

    @GetMapping("user/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public User saveUser(@RequestBody User user) {
        user.setActivityType(UserActivityType.BUYER);
        user.setJoinedAt(new Date());
        return userService.saveUser(user);
    }

//    @PutMapping("/edit")
//    public UserProfile updateProfile(@RequestBody UserProfile userProfile) {
//        return userService.updateProfile(userProfile);
//    }

//    @PostMapping("/auth/register")   //api method post : url :'http://localhost:8080/api/auth/register'
//    public ResponseEntity<User> register(@RequestBody User user){
////        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
//        return ResponseEntity.ok().body(userService.saveUser(user));
//    }
    //    @PostMapping("/role/save")
//    public ResponseEntity<Role> saveRole(@RequestBody Role role){
////        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
//        return ResponseEntity.ok().body(userService.saveRole(role));
//    }
//    @PostMapping("/role/addtouser")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
//        userService.addRoleToUser(form.getUsername(),form.getRoleName());
//        return ResponseEntity.ok().build();
//    }
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
//                        .withClaim("roles", user.getActivity_type().toString())
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
//
//    @GetMapping("user/forgot_password")
//    public String forgotPasswordForm(){
//        return "forgot_password_form";
//    }
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(10);
        try{
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = RequestUtility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            //model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (ResourceNotFoundException ex) {
            return "User not found with email: "+email;
        } catch (UnsupportedEncodingException | MessagingException e) {
            return "Error while sending email";
        }
        return token;
    }
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ducdmhe141516@fpt.edu.vn", "Jovinn support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.getUserByResetPasswordToken(token);
        if(user == null){
            return "Invalid token: "+token;
        }
        else{
            userService.updatePassword(user, password);
            return "You have succcessfully changed your password.";
        }
    }
}
//@Data
//class RoleToUserForm{
//    private String username;
//    private String roleName;
//}
