package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.Gender;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Seller;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    UUID id;
    String title;
    String description;
    Integer impression;
    Integer interesting;
    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;
    UUID sellerId;
    String descriptionBio;
    String sellerNumber;
    @Enumerated(EnumType.STRING)
    RankSeller rankSeller;
    Integer totalOrderFinish;
    Boolean verifySeller;
    UUID userId;
    String firstName;
    String lastName;
    String username;
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
