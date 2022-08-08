package com.jovinn.capstoneproject.dto.adminsite.adminrequest;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLoginRequest {
    @NotBlank(message = "Không được để trống tên tài khoản")
    private String adminAccount;

    @NotBlank(message = "Không được để trống mật khẩu")
    private String password;
}
