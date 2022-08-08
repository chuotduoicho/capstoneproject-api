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
public class UrlProfileRequest {
    @NotBlank(message = "Cung cấp về đường dẫn thông tin của bạn không được để trống")
    @Size(min = 3)
    String title;

    @NotBlank(message = "Đường dẫn thông tin của bạn không được để trống")
    @Size(min = 3)
    String url;

    @NotNull
    UUID userId;
}
