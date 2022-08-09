package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountContractResponse {
    Long totalContract;

    public CountContractResponse(Long totalContract) {
        this.totalContract = totalContract;
    }
}
