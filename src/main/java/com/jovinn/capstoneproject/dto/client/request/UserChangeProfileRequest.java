package com.jovinn.capstoneproject.dto.client.request;

import com.jovinn.capstoneproject.enumerable.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangeProfileRequest {
    String firstName;
    String lastName;
    @Size(min = 10, max = 11, message = "Số điện thoại cần nằm trong khoảng 10 - 11 ký tự")
    String phoneNumber;
    Gender gender;
    Date birthDate;
    String city;
    String country;
    String avatar;
}
