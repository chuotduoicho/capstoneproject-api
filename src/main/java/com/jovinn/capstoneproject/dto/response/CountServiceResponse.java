package com.jovinn.capstoneproject.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountServiceResponse {
    Long totalService;

    public CountServiceResponse(Long totalService) {
        this.totalService = totalService;
    }
}
