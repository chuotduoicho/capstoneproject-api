package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
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
    @JoinColumn(name = "cat_service_id", referencedColumnName = "id")
    Category category;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "sub_cat_service_id", referencedColumnName = "id")
    SubCategory subCategory;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
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
    @JoinColumn(name = "userId", referencedColumnName = "id")
    //@JsonBackReference
    User user;

//    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "post_request_seller",
            joinColumns = @JoinColumn(name = "post_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id", referencedColumnName = "id")
    )
    List<Seller> sellersApplyRequest;

    @OneToMany(mappedBy = "postRequest" ,fetch = FetchType.LAZY)
    List<OfferRequest> offerRequests;
}
