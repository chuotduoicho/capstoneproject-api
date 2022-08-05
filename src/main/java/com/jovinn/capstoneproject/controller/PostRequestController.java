package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.client.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.client.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.client.request.TargetSellerRequest;
import com.jovinn.capstoneproject.dto.client.response.*;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.NotificationService;
import com.jovinn.capstoneproject.service.OfferRequestService;
import com.jovinn.capstoneproject.service.PostRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/postRequest")
@CrossOrigin(origins = "*")
public class PostRequestController {
    @Autowired
    private PostRequestService postRequestService;
    @Autowired
    private OfferRequestService offerRequestService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/addPostRequest")
    public ResponseEntity<ApiResponse> addPostRequestByBuyer(@Valid @RequestBody PostRequestRequest postRequest,
                                                             @CurrentUser UserPrincipal currentUser){
        ApiResponse apiResponse =  postRequestService.addPostRequest(postRequest,currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/getPostRequestByBuyerCreated")
    public List<PostRequestResponse> getPostRequestByBuyerCreated(@CurrentUser UserPrincipal currentUser){
        return postRequestService.getPostRequestByBuyerCreated(currentUser);
    }

    @GetMapping("/get-list-seller-apply/{postRequestId}")
    public ResponseEntity<ListSellerApplyPostRequestResponse> getListSellerApplyRequest(@PathVariable("postRequestId") UUID postRequestId,
                                                                                        @CurrentUser UserPrincipal currentUser) {
        ListSellerApplyPostRequestResponse response = postRequestService.getListSellerApply(postRequestId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getPostRequestByCategoryId/{catId}")
    public ResponseEntity<List<PostRequestResponse>> getPostRequestByCategoryId(@PathVariable("catId") UUID catId) {
        List<PostRequestResponse> response = postRequestService.getPostRequestByCategoryId(catId);
        return new ResponseEntity<>(response, HttpStatus.OK);
        //return postRequestService.getPostRequestByCategoryId(id);
    }

    @GetMapping("/getPostRequestDetails/{postRequestId}")
    public PostRequestResponse getPostRequestDetails(@PathVariable UUID postRequestId){
        return postRequestService.getPostRequestDetails(postRequestId);
    }

    @PutMapping("/updatePostRequest/{postRequestId}")
    public ResponseEntity<ApiResponse> updatePostRequest(@PathVariable UUID postRequestId,@CurrentUser UserPrincipal currentUser,
                                           @RequestBody PostRequestRequest request){
        ApiResponse apiResponse = postRequestService.updatePostRequest(request,postRequestId,currentUser);
       return new ResponseEntity< >(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/sellerApplyRequest/{postRequestId}")
    public ResponseEntity<ApiResponse> sellerApplyRequest(@PathVariable UUID postRequestId,@CurrentUser UserPrincipal currentUser){
        ApiResponse apiResponse = postRequestService.sellerApplyRequest(postRequestId,currentUser);
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @PostMapping("/send-offer/{postRequestId}")
    public ResponseEntity<OfferRequestResponse> sellerSendOffer(@PathVariable UUID postRequestId, @RequestBody OfferRequestRequest request,
                                                                @CurrentUser UserPrincipal currentUser){
        OfferRequestResponse response = offerRequestService.sendOfferToBuyer(postRequestId, request, currentUser);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/apply-post-request/{postRequestId}")
    public ResponseEntity<OfferRequestResponse> sellerApply(@PathVariable UUID postRequestId, @RequestBody OfferRequestRequest request,
                                                                @CurrentUser UserPrincipal currentUser){
        OfferRequestResponse response = offerRequestService.sendOfferApplyToBuyer(postRequestId, request, currentUser);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/get-ten-seller-target")
    public ResponseEntity<List<ListSellerTargetPostRequestResponse>> getTenSellerTarget(@RequestBody TargetSellerRequest request) {
        List<ListSellerTargetPostRequestResponse> responses = postRequestService.getTargetSeller(request);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    @GetMapping("/getAllPostRequest")
    public List<PostRequestResponse> getAllPostRequest(){
        return postRequestService.getAllPostRequest();
    }

    @PostMapping("/sendInvitations")
    public ResponseEntity<String> inviteSeller(@RequestBody ListSellerTargetPostRequestResponse targetSeller){
        notificationService.inviteUser(targetSeller);
        return new ResponseEntity<>("invitation sent", HttpStatus.CREATED);
    }
//    @GetMapping("/getListSellerApply/{postRequestId}")
//    public  ResponseEntity<ListSellerApplyPostRequestResponse> getListSellerApply(@PathVariable UUID postRequestId, @CurrentUser UserPrincipal currentUser){
//        ListSellerApplyPostRequestResponse response = postRequestService.getListSellerApply(postRequestId,currentUser);
//        return new ResponseEntity<>(response,HttpStatus.CREATED);

//    }
}
