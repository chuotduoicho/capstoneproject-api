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
public class LanguageRequest {
    @NotBlank(message = "Ngôn ngữ không được để trống")
    @Size(min = 1, message = "Vui lòng nhập ngôn ngữ của bạn")
    String language;

    @NotNull
    UUID userId;
}
