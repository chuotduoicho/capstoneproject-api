package com.jovinn.capstoneproject.dto.adminsite.adminresponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountUserResponse {
    Long totalUser;

    public CountUserResponse(Long totalUser) {
        this.totalUser = totalUser;
    }
}
