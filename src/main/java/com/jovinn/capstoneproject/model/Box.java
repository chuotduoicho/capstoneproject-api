package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Box extends  BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;

//    @Type(type = "uuid-char")
//    UUID sellerId;

//    @Type(type = "uuid-char")
//    UUID catServiceId;

//    @Type(type = "uuid-char")
//    UUID subCatServiceId;

//    @Type(type = "uuid-char")
//    UUID serviceTypeId;

//    @Type(type = "uuid-char")
//    UUID galleryId;
    String title;
    String description;
    Integer impression;
    Integer interesting;

    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;

//    @ManyToOne(fetch = FetchType.EAGER)
////    //@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "cat_service_id",referencedColumnName = "id")
////    //@JsonIgnore
////    //@MapsId
//    //@JsonBackReference
//    //@JsonIgnore
//    Category category;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
//    @JsonBackReference
    Seller seller;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "sub_cat_service_id", referencedColumnName = "id")
//    @JsonBackReference
    SubCategory subCategory;

//    @ManyToOne(fetch = FetchType.EAGER)
//    //@ManyToOne(optional = false)
//    //@OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "service_type_id", referencedColumnName = "id")
//    //@JsonIgnore
//    //@MapsId
//    @JsonBackReference
//    ServiceType serviceType;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(mappedBy = "box")
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    List<Package> packages;

    @OneToOne(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    Gallery gallery;
}
