package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountTotalRevenueResponse {
    BigDecimal totalRevenue;

    public CountTotalRevenueResponse(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
