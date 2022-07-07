package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.PaymentConfirmStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Wallet extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;

    BigDecimal income;
    BigDecimal withdraw;
    @Enumerated(EnumType.STRING)
    PaymentConfirmStatus confirmPayStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "wallet")
    @JsonManagedReference
    List<Transaction> transactions;
}

