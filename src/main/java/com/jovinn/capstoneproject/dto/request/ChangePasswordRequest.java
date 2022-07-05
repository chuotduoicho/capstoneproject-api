package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotNull String oldPass;

    @NotNull
    @Size(min = 6, max = 30)
    String newPass;

    @Size(min = 6, max = 30)
    @NotNull String rePass;
}
