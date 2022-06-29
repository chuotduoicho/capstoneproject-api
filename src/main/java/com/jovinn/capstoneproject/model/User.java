package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.AuthTypeUser;
import com.jovinn.capstoneproject.enumerable.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class User extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String firstName;
    String lastName;
    String username;
    @Column(unique = true, length = 55, nullable = false)
    String email;
    @Column(unique = true, length = 15)
    String phoneNumber;

    @JsonIgnore
    String verificationCode;
    @JsonIgnore
    Boolean isEnabled;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Temporal(TemporalType.DATE)
    Date birthDate;

    String city;
    String country;
    String avatar;

    @JsonIgnore
    @Size(max = 100)
    String password;

    @Temporal(TemporalType.DATE)
    Date joinedAt;
    @Temporal(TemporalType.DATE)
    Date lastLogin;
    @Temporal(TemporalType.DATE)
    Date joinSellingAt;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    AuthTypeUser authType;

    @JsonIgnore
    String resetPasswordToken;
//    @Enumerated(EnumType.STRING)
//    UserActivityType activityType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonIgnore
    @JoinTable(
            name = "user_activity_type",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "activityTypeId")
    )
    Set<ActivityType> activityType = new HashSet<>();

    //    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    @JsonManagedReference
//    Buyer buyer;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "buyerId")
//    Buyer buyer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    @JsonIgnore
    Seller seller;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<PostRequest> postRequests;
}
