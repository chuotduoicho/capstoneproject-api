package com.jovinn.capstoneproject.dto;

import com.jovinn.capstoneproject.enumerable.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserProfile {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Gender gender;
    Date birthDate;
    String address;
    String province;
    String city;
    String country;
    String avatar;
}
