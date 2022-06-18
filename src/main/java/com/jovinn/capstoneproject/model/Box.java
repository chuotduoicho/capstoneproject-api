package com.jovinn.capstoneproject.model;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @Type(type = "uuid-char")
    UUID sellerId;

    @Type(type = "uuid-char")
    UUID catServiceId;

    @Type(type = "uuid-char")
    UUID subCatServiceId;

    @Type(type = "uuid-char")
    UUID serviceTypeId;

    @Type(type = "uuid-char")
    UUID galleryId;

    String description;
    Integer impression;
    Integer interesting;

    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(mappedBy = "box")
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    List<Package> packages;

}
