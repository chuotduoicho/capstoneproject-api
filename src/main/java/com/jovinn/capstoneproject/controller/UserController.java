package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.request.ChangePasswordRequest;
import com.jovinn.capstoneproject.dto.request.ResetPasswordRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SellerService;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private WalletService walletService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UUID id = currentUser.getId();
        User user = userService.getByUserId(id);
        if (user == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not found in database!!!");
        }
        return new ResponseEntity< >(user, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public User getUserProfile(@PathVariable UUID id) {
        return userService.getByUserId(id);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getUSerProfileByUserName(@PathVariable String username) {
        UserProfile userProfile = userService.getUserProfile(username);
        return new ResponseEntity< >(userProfile, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getListUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User editUser,
                                           @PathVariable("id") UUID id,
                                           @CurrentUser UserPrincipal currentUser) {
        User updatedUser = userService.update(editUser, id, currentUser);
        return new ResponseEntity< >(updatedUser, HttpStatus.CREATED);
    }

    @PostMapping("/join-selling")
    public ResponseEntity<Seller> joinSelling(@RequestBody Seller seller,
                                                   @CurrentUser UserPrincipal currentUser) {
        Seller sellerInfo = sellerService.becomeSeller(seller, currentUser);
        return new ResponseEntity<>(sellerInfo, HttpStatus.CREATED);
    }

    @GetMapping("/wallet")
    public ResponseEntity<Wallet> getWallet(@CurrentUser UserPrincipal currentUser) {
        Wallet response = walletService.getWallet(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String password = request.getPassword();
        User user = userService.getUserByResetPasswordToken(token);
        if (user == null) {
            return "Invalid token: " + token;
        } else {
            userService.updatePassword(user, password);
            return "You have succcessfully changed your password.";
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = userService.changePassword(request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.CREATED);
    }
}
