package com.jovinn.capstoneproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    Date create_at;

    @Temporal(TemporalType.TIMESTAMP)
    Date updated_at;

    @PrePersist
    public void prePersist() {
        this.create_at = this.updated_at = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = new Date();
    }
}
