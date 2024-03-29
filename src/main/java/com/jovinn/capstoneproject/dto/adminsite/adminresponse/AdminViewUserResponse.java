package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminViewUserResponse {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String userName;
    Boolean isEnabled;

    public AdminViewUserResponse(UUID id, String firstName, String lastName, String email, String phoneNumber, String userName,Boolean isEnabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.isEnabled = isEnabled;
    }
}
