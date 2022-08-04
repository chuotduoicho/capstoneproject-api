package com.jovinn.capstoneproject.dto.client.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvatarResponse {
    String avatarBuyer;
    String avatarSeller;

    public AvatarResponse(String avatarBuyer, String avatarSeller) {
        this.avatarBuyer = avatarBuyer;
        this.avatarSeller = avatarSeller;
    }
}
