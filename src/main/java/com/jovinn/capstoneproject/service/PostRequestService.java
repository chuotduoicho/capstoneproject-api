package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.PostRequestResponse;
import com.jovinn.capstoneproject.model.PostRequest;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface PostRequestService {
    //Add new Post Request
    ApiResponse addPostRequest(PostRequestRequest request, UserPrincipal currentUser);

    //Update Post Request
    PostRequest updatePostRequest(PostRequest postRequest, UUID id);

    //Delete Post Request
    Boolean deletePostRequest(UUID id);

    //Buyer view their request list created
    List<PostRequestResponse> getPostRequestByBuyerCreated(UserPrincipal currentUser);

    //View list post request by category id
    List<PostRequestResponse> getPostRequestByCategoryId(UUID categoryId);

    //View Post Request Detail
    PostRequestResponse getPostRequestDetails(UUID postRequestId);
}
