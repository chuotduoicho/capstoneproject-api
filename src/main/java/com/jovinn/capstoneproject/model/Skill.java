package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.SkillLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Skill extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String name;
    @Enumerated(EnumType.STRING)
    SkillLevel level;
    String shortDescribe;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId", referencedColumnName = "id")
    @JsonManagedReference
    Seller seller;
}
