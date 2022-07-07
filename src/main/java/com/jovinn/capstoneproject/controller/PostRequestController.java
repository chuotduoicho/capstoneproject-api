package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.PostRequestResponse;
import com.jovinn.capstoneproject.model.PostRequest;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.PostRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/postRequest")
@CrossOrigin(origins = "*")
public class PostRequestController {
    @Autowired
    private PostRequestService postRequestService;

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

    @GetMapping("/getPostRequestByCategoryId/{id}")
    public List<PostRequestResponse> getPostRequestByCategoryId(@PathVariable UUID id){
        return postRequestService.getPostRequestByCategoryId(id);
    }

    @GetMapping("/getPostRequestDetails/{postRequestId}")
    public PostRequestResponse getPostRequestDetails(@PathVariable UUID postRequestId){
        return postRequestService.getPostRequestDetails(postRequestId);
    }

//    @GetMapping("/sendInviteSellerViewPost/{sellerId}")
//    public String sendInviteSellerViewPost(@PathVariable UUID sellerId,@CurrentUser UserPrincipal currentUser){
//        try{
//
//        }catch(UnsupportedEncodingException | MessagingException e){
//
//        }
//    }
}
