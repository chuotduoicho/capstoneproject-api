package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationRequest {
    @NotBlank(message = "Quốc gia không được để trống")
    String country;

    @NotBlank(message = "Tên trường không được để trống")
    String universityName;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 3, message = "Tên tiêu đề không được để trống")
    String title;

    @NotBlank(message = "Chuyên ngành của bạn không được để trống")
    @Size(min = 1, message = "Chuyên ngành của bạn không được để trống")
    String major;

    Date yearOfGraduation;

    Date fromDate;

    Date toDate;

    Boolean opened;

    @NotNull
    UUID userId;
}
