package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.model.PostRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSellerApplyPostRequestResponse {
    PostRequest postRequest;
    UUID sellerId;

    public ListSellerApplyPostRequestResponse(PostRequest postRequest, UUID sellerId) {
        this.postRequest = postRequest;
        this.sellerId = sellerId;
    }
}
