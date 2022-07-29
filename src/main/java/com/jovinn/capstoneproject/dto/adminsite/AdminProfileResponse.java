package com.jovinn.capstoneproject.dto.adminsite;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminProfileResponse {
    UUID id;
    String firstName;
    String lastName;
    String adminAccount;
    String phoneNumber;
    Double jCoin;

    public AdminProfileResponse(UUID id, String firstName, String lastName, String adminAccount, String phoneNumber, Double jCoin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adminAccount = adminAccount;
        this.phoneNumber = phoneNumber;
        this.jCoin = jCoin;
    }
}
