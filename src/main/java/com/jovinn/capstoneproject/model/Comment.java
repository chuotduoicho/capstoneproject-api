package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @Type(type = "uuid-char")
    UUID userId;
    @Enumerated(EnumType.STRING)
    UserActivityType type;
    String name;
    @Size(min = 1, max = 255)
    String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", referencedColumnName = "id")
    @JsonBackReference
    Contract contract;

    public Comment(UUID userId, UserActivityType type, String name,
                   String text, Contract contract) {
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.text = text;
        this.contract = contract;
    }

    @JsonIgnore
    public Contract getContract() {
        return contract;
    }
}
