package com.jovinn.capstoneproject.dto;

import com.jovinn.capstoneproject.enumerable.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    UUID id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    Gender gender;
    Date birthDate;
    String city;
    String country;
    String avatar;
}
