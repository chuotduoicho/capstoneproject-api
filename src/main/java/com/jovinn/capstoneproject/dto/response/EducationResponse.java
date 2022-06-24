package com.jovinn.capstoneproject.dto.response;

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
public class EducationResponse {
    @NotNull UUID id;
    String country;
    String universityName;
    String title;
    String major;
    Date yearOfGraduation;
    Date fromDate;
    Date toDate;
    Boolean opened;
    UUID sellerId;

    public EducationResponse(UUID id, String country,
                             String universityName, String title,
                             String major, Date yearOfGraduation,
                             Date fromDate, Date toDate,
                             Boolean opened, UUID sellerId) {
        this.id = id;
        this.country = country;
        this.universityName = universityName;
        this.title = title;
        this.major = major;
        this.yearOfGraduation = yearOfGraduation;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.opened = opened;
        this.sellerId = sellerId;
    }
}
