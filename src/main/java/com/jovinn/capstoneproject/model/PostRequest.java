package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class PostRequest extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "cat_service_id", referencedColumnName = "id")
//    @JsonBackReference
    Category category;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "sub_cat_service_id", referencedColumnName = "id")
//    @JsonBackReference
    SubCategory subCategory;

    @ManyToMany(fetch = FetchType.EAGER,cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "post_request_skill",
            joinColumns = @JoinColumn(name = "post_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    List<Skill> skills;
    String recruitLevel;
    String jobTitle;
    String shortRequirement;
    String attachFile;

    @OneToMany(mappedBy = "postRequest",cascade = CascadeType.ALL, orphanRemoval = true)
    List<MilestoneContract> milestoneContracts;

    Integer contractCancelFee;
    Double budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @JsonBackReference
    User user;
}
