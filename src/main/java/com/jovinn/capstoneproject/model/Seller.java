package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.*;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String brandName;
    String descriptionBio;
    String sellerNumber;
    @Enumerated(EnumType.STRING)
    RankSeller rankSeller;
    Integer totalOrderFinish;
    Integer ratingPoint;
    Boolean verifySeller;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Certificate> certificates;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Education> educations;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Language> languages;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Skill> skills;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<UrlProfile> urlProfiles;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    List<Box> boxes;

    @ManyToMany(mappedBy = "sellersApplyRequest")
    @JsonIgnore
    List<PostRequest> postRequests;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    List<Contract> contracts;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    List<OfferRequest> offerRequests;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    List<Rating> ratings;
}
