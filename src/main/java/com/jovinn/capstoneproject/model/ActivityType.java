package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    UserActivityType activityType;

//    @ManyToMany(mappedBy = "activityType", fetch = FetchType.EAGER)
//    //@JsonBackReference
//    List<User> users = new ArrayList<>();

    public ActivityType(UserActivityType activityType) {
        this.activityType = activityType;
    }
}
