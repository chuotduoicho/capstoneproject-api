package com.jovinn.capstoneproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
