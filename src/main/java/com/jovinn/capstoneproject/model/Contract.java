package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovinn.capstoneproject.enumerable.ContractType;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
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
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    @Type(type = "uuid-char")
    UUID packageId;
    String contractCode;
    String requirement;
    Integer quantity;
    Double serviceFee;
    Double subTotal;
    Double serviceDeposit;
    Double totalPrice;
    Integer totalDeliveryTime;
    @Temporal(TemporalType.DATE)
    Date expectCompleteDate;
    @Enumerated(EnumType.STRING)
    DeliveryStatus deliveryStatus;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    @Enumerated(EnumType.STRING)
    ContractType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    //@JsonBackReference
    Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    //@JsonBackReference
    Seller seller;

    public Contract(UUID packageId, String contractCode, String requirement,
                    Integer quantity, Double serviceFee, Double subTotal,
                    Double serviceDeposit, Double totalPrice, Integer totalDeliveryTime,
                    Date expectCompleteDate, DeliveryStatus deliveryStatus,
                    OrderStatus status, ContractType type, Buyer buyer, Seller seller) {
        this.packageId = packageId;
        this.contractCode = contractCode;
        this.requirement = requirement;
        this.quantity = quantity;
        this.serviceFee = serviceFee;
        this.subTotal = subTotal;
        this.serviceDeposit = serviceDeposit;
        this.totalPrice = totalPrice;
        this.totalDeliveryTime = totalDeliveryTime;
        this.expectCompleteDate = expectCompleteDate;
        this.deliveryStatus = deliveryStatus;
        this.status = status;
        this.type = type;
        this.buyer = buyer;
        this.seller = seller;
    }

    @JsonIgnore
    public Buyer getBuyer() {
        return buyer;
    }
    @JsonIgnore
    public Seller getSeller() {
        return seller;
    }
}
