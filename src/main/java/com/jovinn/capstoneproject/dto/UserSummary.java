package com.jovinn.capstoneproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSummary {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
}