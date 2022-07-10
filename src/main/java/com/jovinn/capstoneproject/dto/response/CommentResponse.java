package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    UUID id;
    UUID userId;
    UUID contractId;
    UserActivityType type;
    String name;
    String text;

    public CommentResponse(UUID id, UUID userId, UUID contractId,
                           UserActivityType type, String name, String text) {
        this.id = id;
        this.userId = userId;
        this.contractId = contractId;
        this.type = type;
        this.name = name;
        this.text = text;
    }
}
