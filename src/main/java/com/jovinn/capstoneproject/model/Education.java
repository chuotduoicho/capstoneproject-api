package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Education extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String country;
    String universityName;
    String title;
    String major;
    @Temporal(TemporalType.TIMESTAMP)
    Date yearOfGraduation;
    @Temporal(TemporalType.TIMESTAMP)
    Date fromDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date toDate;
    Boolean opened;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId", referencedColumnName = "id")
    //@JsonManagedReference
    @JsonBackReference
    Seller seller;

    public Education(String country, String universityName, String title, String major, Date yearOfGraduation, Date fromDate, Date toDate, Boolean opened, Seller seller) {
        this.country = country;
        this.universityName = universityName;
        this.title = title;
        this.major = major;
        this.yearOfGraduation = yearOfGraduation;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.opened = opened;
        this.seller = seller;
    }

    @JsonIgnore
    public Seller getSeller() {
        return seller;
    }
}
