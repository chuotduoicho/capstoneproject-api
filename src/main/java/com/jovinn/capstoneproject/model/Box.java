package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Box extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;

    String title;
    String description;
    Integer impression;
    Integer interesting;
    Integer totalFinalContract;

    BigDecimal fromPrice;
    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;

    @ManyToOne(fetch =  FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    Seller seller;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id")
    SubCategory subCategory;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    List<Package> packages;

    @OneToOne(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    Gallery gallery;

    @OneToMany(mappedBy = "box", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Rating> ratings;
    public Box(String title, String description, Integer impression, Integer interesting, BoxServiceStatus status, Seller seller, SubCategory subCategory) {
        this.title = title;
        this.description = description;
        this.impression = impression;
        this.interesting = interesting;
        this.status = status;
        this.seller = seller;
        this.subCategory = subCategory;
    }
}
