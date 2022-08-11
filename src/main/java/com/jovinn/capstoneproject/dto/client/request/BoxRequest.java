package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoxRequest {
    @NotBlank(message = "Không được để trống tiêu đề")
    String title;
    @NotBlank(message = "Không được để trống mô tả cho hộp dịch vụ")
    String description;
    UUID subCategoryId;
    String imageGallery1;
    String imageGallery2;
    String imageGallery3;
    String videoGallery;
    String documentGallery;
}
