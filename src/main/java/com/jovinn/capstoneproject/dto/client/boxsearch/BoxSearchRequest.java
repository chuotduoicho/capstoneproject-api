package com.jovinn.capstoneproject.dto.client.boxsearch;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoxSearchRequest extends BaseSearchRequest {
    UUID categoryId;
    UUID subCategoryId;
    String searchKeyWord;
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
