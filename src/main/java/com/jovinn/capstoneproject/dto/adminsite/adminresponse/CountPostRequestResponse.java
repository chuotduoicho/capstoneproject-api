package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountPostRequestResponse {
    Long totalPostRequest;

    public CountPostRequestResponse(Long totalPostRequest) {
        this.totalPostRequest = totalPostRequest;
    }
}
