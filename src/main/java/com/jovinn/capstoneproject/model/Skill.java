package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovinn.capstoneproject.enumerable.SkillLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", referencedColumnName = "id")
    @JsonBackReference
    Seller seller;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "post_request_skill",
            joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_request_id", referencedColumnName = "id")
    )
    List<PostRequest> postRequests;

    public Skill(String name, SkillLevel level, String shortDescribe, Seller seller) {
        this.name = name;
        this.level = level;
        this.shortDescribe = shortDescribe;
        this.seller = seller;
    }

    @JsonIgnore
    public Seller getSeller() {
        return seller;
    }
}
