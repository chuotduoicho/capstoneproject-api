package com.jovinn.capstoneproject.dto.adminsite.adminrequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSignupRequest {
    String firstName;
    String lastName;
    String username;
    String password;
    String email;
    String phoneNumber;
}
