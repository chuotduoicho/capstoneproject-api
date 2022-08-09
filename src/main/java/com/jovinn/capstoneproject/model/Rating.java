package com.jovinn.capstoneproject.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Rating extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @Type(type = "uuid-char")
    UUID buyerId;
    @Type(type = "uuid-char")
    UUID sellerId;
    @Min(1)
    @Max(5)
    Integer ratingPoint;
    String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boxId", referencedColumnName = "id")
    Box box;
}
