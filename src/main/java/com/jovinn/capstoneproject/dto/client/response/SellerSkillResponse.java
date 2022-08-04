package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.model.Skill;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SellerSkillResponse {
    UUID sellerId;
    String firstName;
    String lastName;
    String city;
    List<Skill> skills;
    String avatar;
}
