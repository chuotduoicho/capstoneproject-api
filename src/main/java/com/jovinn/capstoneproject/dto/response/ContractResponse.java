package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
    @NotNull UUID id;
    UUID packageId;
    String contractNumber;
    String requirement;
    Integer quantity;
    Double serviceFee;
    Double subTotal;
    Double serviceDeposit;
    Double totalPrice;
    Integer totalDeliveryTime;
    Date expectCompleteDate;
    DeliveryStatus deliveryStatus;
    OrderStatus status;

    UUID buyerId;
    UUID sellerId;

    public ContractResponse(UUID id, UUID packageId, String contractNumber,
                            String requirement, Integer quantity, Double serviceFee,
                            Double subTotal, Double serviceDeposit, Double totalPrice,
                            Integer totalDeliveryTime, Date expectCompleteDate,
                            DeliveryStatus deliveryStatus, OrderStatus status,
                            UUID buyerId, UUID sellerId) {
        this.id = id;
        this.packageId = packageId;
        this.contractNumber = contractNumber;
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
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }
}
