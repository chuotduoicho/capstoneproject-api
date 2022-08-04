package com.jovinn.capstoneproject.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Không được để trống tên tài khoản hoặc email")
    private String usernameOrEmail;

    @NotBlank(message = "Không được để trống mật khẩu")
    private String password;
}
