package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Package extends  BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String title;

    @Size(min = 20, max = 500)
    String shortDescription;

    @Min(1)
    Integer deliveryTime;

    @Min(1)
    BigDecimal price;

    @Min(0)
    @Max(100)
    Integer contractCancelFee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "box_service_id",referencedColumnName = "id")
    //@JsonIgnore
    @JsonBackReference
    Box box;

//    @OneToMany(mappedBy = "pack", fetch = FetchType.EAGER)
//    List<PackageOptional> packageOptionals;
}
