package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.model.Seller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSellerApplyPostRequestResponse {
    UUID postRequestId;
    List<Seller> sellersApply;

    public ListSellerApplyPostRequestResponse(UUID postRequestId, List<Seller> sellersApply) {
        this.postRequestId = postRequestId;
        this.sellersApply = sellersApply;
    }
}
