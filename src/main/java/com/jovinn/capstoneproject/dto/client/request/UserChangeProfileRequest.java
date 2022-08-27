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
    @Size(max = 10, message = "Số điện thoại tối đa 10 ký tự")
    String phoneNumber;
    Gender gender;
    Date birthDate;
    String city;
    String country;
    String avatar;
}
