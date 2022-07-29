package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.response.*;
import com.jovinn.capstoneproject.model.PostRequest;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface PostRequestService {
    //Add new Post Request
    ApiResponse addPostRequest(PostRequestRequest request, UserPrincipal currentUser);

    //Update Post Request
    ApiResponse updatePostRequest(PostRequestRequest postRequest, UUID id, UserPrincipal currentUser);

    //Delete Post Request
    Boolean deletePostRequest(UUID id);

    //Buyer view their request list created
    List<PostRequestResponse> getPostRequestByBuyerCreated(UserPrincipal currentUser);

    //View list post request by category id
    PageResponse<PostRequestResponse> getPostRequestByCategoryId(UUID categoryId, int page, int size, String sortBy, String sortDir);

    //View Post Request Detail
    PostRequestResponse getPostRequestDetails(UUID postRequestId);

    //Seller Apply Request
    ApiResponse sellerApplyRequest(UUID postRequestId, UserPrincipal currentUser);

    ListSellerApplyPostRequestResponse getListSellerApply(UUID postRequestId, UserPrincipal currentUser);

    List<PostRequestResponse> getAllPostRequest();

    CountPostRequestResponse countTotalPostRequestByCatId(UUID catId);
}
