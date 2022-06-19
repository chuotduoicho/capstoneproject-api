package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Temporal(TemporalType.DATE)
    Date birthDate;

    String address;
    String province;
    String city;
    String country;
    String avatar;
    @Size(max = 100)
    String password;

    @Temporal(TemporalType.DATE)
    Date joinedAt;
    @Temporal(TemporalType.DATE)
    Date lastLogin;
    @Temporal(TemporalType.DATE)
    Date joinSellingAt;

    @Enumerated(EnumType.STRING)
    AuthTypeUser authType;

    String resetPasswordToken;
//    @Enumerated(EnumType.STRING)
//    UserActivityType activityType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(
            name = "user_activity_type",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "activityTypeId")
    )
    Set<ActivityType> activityType = new HashSet<>();
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
}
