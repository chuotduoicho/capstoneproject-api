package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoxRequest {
    @NotBlank(message = "Không được để trống tiêu đề")
    @Size(max = 50, message = "không được nhập quá 50 ký tự")
    String title;
    @NotBlank(message = "Không được để trống mô tả cho hộp dịch vụ")
    @Size(max = 5000, message = "không được nhập quá 5000 ký tự")
    String description;
    UUID subCategoryId;
    String imageGallery1;
    String imageGallery2;
    String imageGallery3;
    String videoGallery;
    String documentGallery;
}
