package com.jovinn.capstoneproject.dto.client.boxsearch;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseSearchRequest {
    @Min(value = 1, message = "Vị trí trang không được nhỏ hơn 1")
    int page = 1;

    public int getPage() {
        return page - 1;
    }

    @Min(value = 1, message = "Size của trang cần lớn hơn 1")
    @Max(value = 100, message = "Size của trang cần nhỏ hơn 100")
    int size = 12;
    @Builder.Default
    Sort.Direction sortDirection = Sort.Direction.DESC; //default
}
