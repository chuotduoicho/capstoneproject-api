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
    Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @PrePersist
    public void prePersist() {
        this.createAt = this.updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}
