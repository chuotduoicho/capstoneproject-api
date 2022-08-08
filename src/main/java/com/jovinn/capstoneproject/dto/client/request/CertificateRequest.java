package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 1, message = "Tiêu đề không được để trống")
    String title;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 1, message = "Tên chứng nhận không được để trống")
    String name;

    @NotBlank(message = "Đường dẫn không được để trống")
    @Size(min = 1, message = "Đường dẫn không được để trống")
    String linkCer;

    @NotNull
    UUID userId;
}
