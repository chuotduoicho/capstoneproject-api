package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    UUID userId;
    String descriptionBio;
    String sellerNumber;
    @Enumerated(EnumType.STRING)
    RankSeller rankSeller;
    Boolean verifySeller;

//    @OneToMany(mappedBy = "seller",
//            fetch = FetchType.LAZY,
//            orphanRemoval = true)
//    List<Language> languages;
//
//    @OneToMany(mappedBy = "seller",
//            cascade = CascadeType.PERSIST,
//            orphanRemoval = true)
//    List<Education> educations;
//
//    @OneToMany(mappedBy = "seller",
//            cascade = CascadeType.PERSIST,
//            orphanRemoval = true)
//    List<Skill> skills;
//
//    @OneToMany(mappedBy = "seller",
//            cascade = CascadeType.PERSIST,
//            orphanRemoval = true)
//    List<Certificate> certificates;
//
//    @OneToMany(mappedBy = "seller",
//            cascade = CascadeType.PERSIST,
//            orphanRemoval = true)
//    List<UrlProfile> urlProfiles;
}
