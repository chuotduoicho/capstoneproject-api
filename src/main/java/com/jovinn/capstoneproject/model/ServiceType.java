package com.jovinn.capstoneproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ServiceType extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID sub_cat_service_id;
    String name;
}
