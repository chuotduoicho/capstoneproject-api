package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRevenueByMonth {
    String month;
    BigDecimal revenue;

    public AdminRevenueByMonth(String month, BigDecimal revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}
