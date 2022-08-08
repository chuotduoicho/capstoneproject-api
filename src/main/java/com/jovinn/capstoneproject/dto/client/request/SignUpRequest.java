package com.jovinn.capstoneproject.dto.client.request;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank(message = "Họ Không được để trống")
    @Size(min = 1, max = 40)
    private String firstName;

    @NotBlank(message = "Tên Không được để trống")
    @Size(min = 1, max = 40)
    private String lastName;

    @NotBlank(message = "Tên tài khoản Không được để trống")
    @Size(min = 5, max = 50, message = "Tên tài khoản cần nhập đủ 5 ký tự trở lên")
    @Pattern(regexp = "^[a-zA-Z0-9äöüÄÖÜ]*$", message = "Tên tài khoản không được chứa ký tự đặc biệt")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Size(max = 255)
    @Email
    @Pattern(regexp=".+@.+\\..+", message = "Hãy nhập đúng định dạng email")
    private String email;

    @NotBlank(message = "Mật khẩu Không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu cần ít nhất 6 ký tự và nhiều nhất 30 ký tự")
    private String password;
}
