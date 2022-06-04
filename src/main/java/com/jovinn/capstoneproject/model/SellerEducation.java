package com.jovinn.capstoneproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

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
public class SellerEducation extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    String universityName;
    String title;
    String major;
    String country;
    @Temporal(TemporalType.TIMESTAMP)
    Date yearOfGraduation;
    @Temporal(TemporalType.TIMESTAMP)
    Date fromDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date toDate;
    Boolean opened;

    @ManyToOne(fetch = FetchType.LAZY)
    Seller seller;
}
