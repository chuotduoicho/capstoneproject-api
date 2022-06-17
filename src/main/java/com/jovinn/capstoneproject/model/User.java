package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.AuthTypeUser;
import com.jovinn.capstoneproject.enumerable.Gender;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
    String phone_number;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Temporal(TemporalType.DATE)
    Date birth_date;

    String address;
    String province;
    String city;
    String country;
    String avatar;
    String password;

    @Temporal(TemporalType.DATE)
    Date joined_at;
    @Temporal(TemporalType.DATE)
    Date last_login;
    @Temporal(TemporalType.DATE)
    Date join_selling_at;

    @Enumerated(EnumType.STRING)
    AuthTypeUser authType;

    @Enumerated(EnumType.STRING)
    UserActivityType activityType;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "user_activity_type",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "activityTypeId")
//    )
//    Set<ActivityType> activityType = new HashSet<>();
//
//    public void addActivityType(ActivityType activityType) {
//        activityTypes.add(activityType);
//        activityType.getUsers().add(this);
//    }
//
//    public void removeActivityType(ActivityType activityType) {
//        activityTypes.remove(activityType);
//        activityType.getUsers().remove(this);
//    }
//    public Set<ActivityType> getActivityTypes() {
//        return activityTypes;
//    }

//    public void setActivityTypes(Set<ActivityType> activityTypes) {
//        this.activityTypes = activityTypes;
//    }

    //    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    @JsonManagedReference
//    Buyer buyer;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyerId")
    Buyer buyer;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Seller seller;

    String resetPasswordToken;
}
