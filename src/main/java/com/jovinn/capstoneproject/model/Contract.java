package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.ContractType;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
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
    Integer contractCancelFee;
    BigDecimal serviceDeposit;
    BigDecimal totalPrice;
    Integer totalDeliveryTime;
    @Temporal(TemporalType.DATE)
    Date expectCompleteDate;
    @Enumerated(EnumType.STRING)
    DeliveryStatus deliveryStatus;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    ContractStatus contractStatus;
    @Enumerated(EnumType.STRING)
    ContractType type;
    Boolean flag;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    Buyer buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    Seller seller;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    List<ExtraOffer> extraOffers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "contract")
    List<Delivery> delivery;

    @OneToOne(mappedBy = "contract", fetch = FetchType.LAZY)
    PostRequest postRequest;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @JsonManagedReference
    @JsonIgnore
    List<Comment> comments;
//
//    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER)
//    List<MilestoneContract> milestoneContracts;

    public Contract(UUID packageId, String contractCode, String requirement,
                    Integer quantity, Integer contractCancelFee, BigDecimal serviceDeposit,
                    BigDecimal totalPrice, Integer totalDeliveryTime,
                    Date expectCompleteDate, DeliveryStatus deliveryStatus,
                    OrderStatus orderStatus, ContractStatus contractStatus,
                    ContractType type, Buyer buyer, Seller seller, Boolean flag) {
        this.packageId = packageId;
        this.contractCode = contractCode;
        this.requirement = requirement;
        this.quantity = quantity;
        this.contractCancelFee = contractCancelFee;
        this.serviceDeposit = serviceDeposit;
        this.totalPrice = totalPrice;
        this.totalDeliveryTime = totalDeliveryTime;
        this.expectCompleteDate = expectCompleteDate;
        this.deliveryStatus = deliveryStatus;
        this.orderStatus = orderStatus;
        this.contractStatus = contractStatus;
        this.type = type;
        this.buyer = buyer;
        this.seller = seller;
        this.flag = flag;
    }

    public Contract(String contractCode, String requirement,
                    Integer quantity, Integer contractCancelFee, BigDecimal serviceDeposit,
                    BigDecimal totalPrice, Integer totalDeliveryTime,
                    Date expectCompleteDate, DeliveryStatus deliveryStatus,
                    OrderStatus orderStatus, ContractStatus contractStatus,
                    ContractType type, Buyer buyer, Seller seller, Boolean flag) {
        this.contractCode = contractCode;
        this.requirement = requirement;
        this.quantity = quantity;
        this.contractCancelFee = contractCancelFee;
        this.serviceDeposit = serviceDeposit;
        this.totalPrice = totalPrice;
        this.totalDeliveryTime = totalDeliveryTime;
        this.expectCompleteDate = expectCompleteDate;
        this.deliveryStatus = deliveryStatus;
        this.orderStatus = orderStatus;
        this.contractStatus = contractStatus;
        this.type = type;
        this.buyer = buyer;
        this.seller = seller;
        this.flag = flag;
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
