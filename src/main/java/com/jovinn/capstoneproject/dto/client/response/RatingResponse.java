package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.model.Box;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RatingResponse {
    UUID id;
    Date createAt;
    String avatarBuyer;
    String fullNameBuyer;
    Integer ratingPoint;
    String comment;
    Integer count;

    public RatingResponse(UUID id, Date createAt, String avatarBuyer,
                          String fullNameBuyer, Integer ratingPoint,
                          String comment, Integer count) {
        this.id = id;
        this.createAt = createAt;
        this.avatarBuyer = avatarBuyer;
        this.fullNameBuyer = fullNameBuyer;
        this.ratingPoint = ratingPoint;
        this.comment = comment;
        this.count = count;
    }
}
