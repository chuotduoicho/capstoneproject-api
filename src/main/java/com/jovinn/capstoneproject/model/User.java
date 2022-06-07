package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.Gender;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue( strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    Long id;
    String firstName;
    String lastName;
    String username;
    @Column(unique = true, length = 55)
    String email;
    //@Column(unique = true, length = 15)
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
    UserActivityType activityType = UserActivityType.BUYER;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Collection<Role> roles= new ArrayList<>();
}
