package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSellerTargetPostRequestResponse {
    UUID sellerId;
    String avatar;
    String fullName;
    String branchName;
    Integer totalOrderFinish;
    Integer ratingPoint;
    String skillName;
    RankSeller rankSeller;

    public ListSellerTargetPostRequestResponse(UUID sellerId, String avatar, String fullName,
                                               String branchName, Integer totalOrderFinish,
                                               Integer ratingPoint, String skillName, RankSeller rankSeller) {
        this.sellerId = sellerId;
        this.avatar = avatar;
        this.fullName = fullName;
        this.branchName = branchName;
        this.totalOrderFinish = totalOrderFinish;
        this.ratingPoint = ratingPoint;
        this.skillName = skillName;
        this.rankSeller = rankSeller;
    }
}
