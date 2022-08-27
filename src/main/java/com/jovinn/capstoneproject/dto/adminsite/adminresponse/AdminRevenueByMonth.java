package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRevenueByMonth {
    String name;
    BigDecimal Total;

    public AdminRevenueByMonth(String month, BigDecimal revenue) {
        this.name = month;
        this.Total = revenue;
    }
}
