package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCountDataResponse {
    Long totalUser;
    Long totalService;
    Long totalRequest;
    Long totalContract;
    BigDecimal totalRevenue;

    public AdminCountDataResponse(Long totalUser, Long totalService,Long totalRequest,Long totalContract, BigDecimal totalRevenue) {
        this.totalUser = totalUser;
        this.totalService = totalService;
        this.totalRequest = totalRequest;
        this.totalContract = totalContract;
        this.totalRevenue = totalRevenue;
    }
}
