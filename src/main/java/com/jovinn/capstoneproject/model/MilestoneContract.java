package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.MilestoneStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class MilestoneContract extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String description;
    @Temporal(TemporalType.DATE)
    Date startDate;
    @Temporal(TemporalType.DATE)
    Date endDate;
    @Enumerated(EnumType.STRING)
    MilestoneStatus status;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "contractId", referencedColumnName = "id")
//    Contract contract;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    Delivery delivery;
}
