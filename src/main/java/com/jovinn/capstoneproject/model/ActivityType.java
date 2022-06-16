package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.UserActivityType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class ActivityType {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @Enumerated(EnumType.STRING)
    UserActivityType activityType;

//    @ManyToMany(mappedBy = "activityTypes")
//    List<User> users = new ArrayList<>();

//    public ActivityType(UserActivityType activityType) {
//        this.activityType = activityType;
//    }
}
