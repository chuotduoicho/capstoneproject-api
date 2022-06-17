package com.jovinn.capstoneproject.model;

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

import javax.persistence.*;
import java.util.Date;
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
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String firstName;
    String lastName;
    String username;
    @Column(unique = true, length = 55, nullable = false)
    @NotNull
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
    String password;

    @Temporal(TemporalType.DATE)
    Date joinedAt;
    @Temporal(TemporalType.DATE)
    Date lastLogin;
    @Temporal(TemporalType.DATE)
    Date joinSellingAt;

    @Enumerated(EnumType.STRING)
    UserActivityType activityType;

    String resetPasswordToken;
}
