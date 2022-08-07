package com.jovinn.capstoneproject.dto.client.boxsearch;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoxSearchResponse {
    UUID id;
    Date createAt;
    Date updatedAt;
    Seller seller;
    String imageGallery1;
    String avatar;
    String branchName;
    RankSeller rankSeller;
    Integer totalOrderFinish;
    Integer ratingPoint;
    Integer impression;
    String title;
    BigDecimal fromPrice;
    List<Package> packages;

    public BoxSearchResponse(UUID id, Date createAt, Date updatedAt, Seller seller,
                             String imageGallery1, String avatar, String branchName, RankSeller rankSeller,
                             Integer totalOrderFinish, Integer ratingPoint, Integer impression, String title,
                             BigDecimal fromPrice, List<Package> packages) {
        this.id = id;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.seller = seller;
        this.imageGallery1 = imageGallery1;
        this.avatar = avatar;
        this.branchName = branchName;
        this.rankSeller = rankSeller;
        this.totalOrderFinish = totalOrderFinish;
        this.ratingPoint = ratingPoint;
        this.impression = impression;
        this.title = title;
        this.fromPrice = fromPrice;
        this.packages = packages;
    }

    public String getBranchName() {
        return seller.getBrandName();
    }

    public Integer getRatingPoint() {
        return seller.getRatingPoint();
    }

    public UUID getId() {
        return id;
    }
    public Integer getImpression() {
        return impression;
    }

    public String getImageGallery1() {
        return imageGallery1;
    }

    public String getAvatar() {
        return seller.getUser().getAvatar();
    }

    public RankSeller getRankSeller() {
        return rankSeller;
    }

    public Integer getTotalOrderFinish() {
        return seller.getTotalOrderFinish();
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getFromPrice() {
        return fromPrice == null ? (packages.get(0) == null ? new BigDecimal(0) : packages.get(0).getPrice()) : fromPrice;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

//    public List<Skill> getListSkill() {
//        return seller.getSkills();
//    }
}
