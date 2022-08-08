package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryNotMilestoneRequest {
    String file;
    @NotBlank(message = "Mô tả cho sản phẩm sẽ giúp người mua hiểu rõ hơn")
    String description;
}
