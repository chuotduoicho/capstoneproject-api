package com.jovinn.capstoneproject.dto.adminsite;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminViewContractsResponse {
    String contractCode;
    String userName;
    String fullName;
    BigDecimal totalPrice;
    Date dateCreated;

    public AdminViewContractsResponse(String contractCode, String userName, String fullName, BigDecimal totalPrice, Date dateCreated) {
        this.contractCode = contractCode;
        this.userName = userName;
        this.fullName = fullName;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
    }
}
