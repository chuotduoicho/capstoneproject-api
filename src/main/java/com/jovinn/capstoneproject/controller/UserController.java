package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.client.request.ChangePasswordRequest;
import com.jovinn.capstoneproject.dto.client.request.ResetPasswordRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.NotificationResponse;
import com.jovinn.capstoneproject.dto.client.response.WalletResponse;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
//    @Autowired
//    private WalletService walletService;
    @Autowired
    private OfferRequestService offerRequestService;
    @Autowired
    private NotificationService notificationService;
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
    public ResponseEntity<Seller> joinSelling(@Valid @RequestBody Seller seller,
                                              @CurrentUser UserPrincipal currentUser) {
        Seller sellerInfo = sellerService.becomeSeller(seller, currentUser);
        return new ResponseEntity<>(sellerInfo, HttpStatus.CREATED);
    }

//    @GetMapping("/wallet")
//    public ResponseEntity<WalletResponse> getWallet(@CurrentUser UserPrincipal currentUser) {
//        WalletResponse response = walletService.getWallet(currentUser);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/list-offer/{postRequestId}")
    public ResponseEntity<List<OfferRequest>> getOfferRequests(@PathVariable("postRequestId") UUID postRequestId,
                                                                       @CurrentUser UserPrincipal currentUser) {
        List<OfferRequest> response = offerRequestService.getAllOffersByPostRequest(postRequestId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
        //return offerRequestService.getAllOffersByPostRequest(postRequestId, currentUser);
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
            return "Bạn đã đổi mật khẩu thành công";
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = userService.changePassword(request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.CREATED);
    }

    @PutMapping("/read-noti/{notificationId}")
    public ResponseEntity<ApiResponse> readNotification(@PathVariable("notificationId") UUID notificationId,
                                                        @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = notificationService.readNotification(notificationId, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @GetMapping("/notifications")

    public ResponseEntity<NotificationResponse> getNotifications(@CurrentUser UserPrincipal currentUser) {
        NotificationResponse response = notificationService.getNotifications(currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }
    @GetMapping("/recieve-notify")
    public SseEmitter registerEmitter(){
        return notificationService.registerClient();
    }

    @DeleteMapping("/delete-noti/{notificationId}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable("notificationId") UUID notificationId,
                                          @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = notificationService.deleteNotification(notificationId, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }
}
