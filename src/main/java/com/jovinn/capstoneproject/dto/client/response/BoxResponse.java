package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoxResponse {
    UUID id;
    Date createAt;
    Date updatedAt;
    Seller seller;
    UUID sellerId;
    String title;
    String description;
    Integer impression;
    Integer totalFinalContract;
    Integer interesting;
    BoxServiceStatus status;
    SubCategory subCategory;
    List<Package> packages;
    Gallery gallery;

    public BoxResponse(UUID id, Date createAt, Date updatedAt,
                       Seller seller, UUID sellerId, String title,
                       String description, Integer impression, Integer interesting, Integer totalFinalContract,
                       BoxServiceStatus status, SubCategory subCategory, List<Package> packages, Gallery gallery) {
        this.id = id;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.seller = seller;
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.impression = impression;
        this.interesting = interesting;
        this.status = status;
        this.subCategory = subCategory;
        this.packages = packages;
        this.gallery = gallery;
        this.totalFinalContract = totalFinalContract;
    }

    public Integer getTotalFinalContract() {
        return totalFinalContract;
    }

    public UUID getId() {
        return id;
    }

    public String getBrandName() {
        return seller.getBrandName();
    }
    public UUID getSubCategoryId() {
        return subCategory.getId();
    }
    public String getSubCategoryName() {
        return subCategory.getName();
    }

    public UUID getCategoryId() {
        return subCategory.getCategory().getId();
    }

    public String getCategoryName() {
        return subCategory.getCategory().getName();
    }
    public Integer getRatingPoint() {
        return seller.getRatingPoint();
    }

    public String getLastName() {
        return seller.getUser().getLastName();
    }

    public String getFirstName() {
        return seller.getUser().getFirstName();
    }

    public String getAvatar() {
        return seller.getUser().getAvatar();
    }

    public String getCity() {
        return seller.getUser().getCity();
    }

    public String getCountry() {
        return seller.getUser().getCountry();
    }

    public RankSeller getRankSeller() {
        return seller.getRankSeller();
    }

    public Integer getTotalOrder() {
        return seller.getTotalOrderFinish();
    }

    public String getDescriptionBio() {
        return seller.getDescriptionBio();
    }

    public List<Skill> getListSkill() {
        return seller.getSkills();
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public UUID getUserId() {
        return seller.getUser().getId();
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getLastLogin() {
        return seller.getUser().getLastLogin();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getImpression() {
        return impression;
    }

    public Integer getInteresting() {
        return interesting;
    }

    public BoxServiceStatus getStatus() {
        return status;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public Date getJoinSellingAt() {
        return seller.getUser().getJoinSellingAt();
    }

    public String getPhoneNumber() {
        return seller.getUser().getPhoneNumber();
    }

    public String getEmail() {
        return seller.getUser().getEmail();
    }
}
