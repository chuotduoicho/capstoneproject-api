package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletRequest {
    @NotNull(message = "Số tiền cần nạp không được để trống")
    @Min(value = 1, message = "Số tiền nạp tối thiểu là 1$")
    BigDecimal charge;
    @NotBlank(message = "Vui lòng chọn mệnh giá quy đổi")
    String currency;
    //Auto generate field text is: "You want to buy ** coin in Jovinn"
}
