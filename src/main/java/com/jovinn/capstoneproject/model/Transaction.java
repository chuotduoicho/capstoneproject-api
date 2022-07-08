package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jovinn.capstoneproject.enumerable.TransactionType;
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
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @Type(type = "uuid-char")
    UUID userId;
    BigDecimal amount;
    String description;
    String intent;
    String method;
    String currency;
    String paymentCode;
    String message;
    @Enumerated(EnumType.STRING)
    TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walletId", referencedColumnName = "id")
    @JsonBackReference
    Wallet wallet;
}
