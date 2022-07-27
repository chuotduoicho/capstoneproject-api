package com.jovinn.capstoneproject.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCountDataResponse {
    Long totalUser;
    Long totalService;
    BigDecimal totalRevenue;

    public AdminCountDataResponse(Long totalUser, Long totalService, BigDecimal totalRevenue) {
        this.totalUser = totalUser;
        this.totalService = totalService;
        this.totalRevenue = totalRevenue;
    }
}
