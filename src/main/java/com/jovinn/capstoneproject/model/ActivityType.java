package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Objects;
import java.util.Set;

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

    @ManyToMany(mappedBy = "activityType", fetch = FetchType.EAGER)
    @JsonIgnore
    //@JsonBackReference
    Set<User> users;

    public ActivityType(UserActivityType activityType) {
        this.activityType = activityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityType that = (ActivityType) o;
        return Long.compare(id, that.id) == 0 && activityType == that.activityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activityType);
    }
}
