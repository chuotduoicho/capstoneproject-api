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
public class LanguageRequest {
    @NotBlank
    String language;

    @NotNull
    UUID userId;
}
