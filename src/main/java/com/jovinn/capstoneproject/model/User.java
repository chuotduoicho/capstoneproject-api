package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.Enumerable.Gender;
import com.jovinn.capstoneproject.Enumerable.UserActivityType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    String first_name;
    String last_name;

    @Column(unique = true, length = 55)
    String email;
    @Column(unique = true, length = 15)
    String phone_number;
    @Enumerated(EnumType.STRING)
    Gender gender;
    @Temporal(TemporalType.DATE)
    Date birth_date;
    String avatar;
    String password;
    String address;
    String province;
    String city;
    String country;
    Boolean verify;
    @Temporal(TemporalType.TIMESTAMP)
    Date joined_at;
    @Temporal(TemporalType.TIMESTAMP)
    Date last_login;
    @Temporal(TemporalType.TIMESTAMP)
    Date join_selling_at;

    @Enumerated(EnumType.STRING)
    UserActivityType activity_type;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles= new ArrayList<>();
}
