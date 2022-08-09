package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class PostRequest extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;

    @Enumerated(EnumType.STRING)
    PostRequestStatus status;
    String recruitLevel;
    String jobTitle;
    String shortRequirement;
    String attachFile;
    Integer contractCancelFee;
    Integer totalDeliveryTime;
    BigDecimal budget;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "sub_catgory_id", referencedColumnName = "id")
    SubCategory subCategory;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JsonIgnore
    @JoinTable(
            name = "post_request_skill",
            joinColumns = @JoinColumn(name = "post_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    List<Skill> skills;

    @OneToMany(mappedBy = "postRequest",cascade = CascadeType.ALL, orphanRemoval = true)
    List<MilestoneContract> milestoneContracts;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", referencedColumnName = "id")
    Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId", referencedColumnName = "id")
    //@JsonBackReference
    User user;

//    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JsonIgnore
    @JoinTable(
            name = "post_request_seller",
            joinColumns = @JoinColumn(name = "post_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id", referencedColumnName = "id")
    )
    List<Seller> sellersApplyRequest;

    @OneToMany(mappedBy = "postRequest" ,fetch = FetchType.LAZY)
    @JsonIgnore
    List<OfferRequest> offerRequests;
}
