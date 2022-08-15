package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.client.request.TargetSellerRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerApplyPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerTargetPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.PostRequestResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

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

    List<ListSellerTargetPostRequestResponse> getTargetSeller(TargetSellerRequest request);

    //View list post request by category id
    List<PostRequestResponse> getPostRequestByCategoryId(UUID categoryId, UserPrincipal currentUser);

    //View Post Request Detail
    PostRequestResponse getPostRequestDetails(UUID postRequestId);

    //Seller Apply Request
    ApiResponse sellerApplyRequest(UUID postRequestId, UserPrincipal currentUser);

    ListSellerApplyPostRequestResponse getListSellerApply(UUID postRequestId, UserPrincipal currentUser);

    List<PostRequestResponse> getAllPostRequest();

    CountPostRequestResponse countTotalPostRequestByCatId(UUID catId);
}
