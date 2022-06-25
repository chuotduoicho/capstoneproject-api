package com.jovinn.capstoneproject.dto.request;

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
    @NotBlank
    @Size(min = 3)
    String title;

    @NotBlank
    @Size(min = 3)
    String name;

    @NotBlank
    @Size(min = 3)
    String linkCer;

    @NotNull
    UUID userId;
}
