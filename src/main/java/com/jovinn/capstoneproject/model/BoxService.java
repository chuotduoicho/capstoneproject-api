package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.Enumerable.BoxServiceStatus;
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
public class BoxService extends  BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID seller_id;
    UUID cat_service_id;
    UUID package_id;
    UUID gallery_id;
    String description;
    Integer impression;
    Integer interesting;

    @Enumerated(EnumType.STRING)
    BoxServiceStatus status;
}
