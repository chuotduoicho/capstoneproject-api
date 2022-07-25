package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotNull(message = "Mật khẩu không được để trống")
    String oldPass;

    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu cần ít nhất 6 ký tự trở lên")
    String newPass;

    @NotNull(message = "Mật khẩu nhắc lại không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu cần ít nhất 6 ký tự và trùng với mật khẩu mới")
    String rePass;
}
