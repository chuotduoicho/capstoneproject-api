package com.jovinn.capstoneproject.dto.request;

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
    @NotBlank
    String country;

    @NotBlank
    String universityName;

    @NotBlank
    @Size(min = 3)
    String title;

    @NotBlank
    String major;

    Date yearOfGraduation;

    Date fromDate;

    Date toDate;

    Boolean opened;

    @NotNull
    UUID userId;
}
