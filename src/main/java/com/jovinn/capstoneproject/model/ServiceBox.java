package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class ServiceBox extends  BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID sellerId;
    UUID catServiceId;
    UUID packageId;
    UUID galleryId;
    String description;
    Integer impression;
    Integer interesting;

    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;
}