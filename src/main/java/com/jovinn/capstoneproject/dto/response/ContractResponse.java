package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
    @NotNull UUID id;
    UUID packageId;
    String contractCode;
    String requirement;
    Integer quantity;
    Integer contractCancelFee;
    BigDecimal serviceDeposit;
    BigDecimal totalPrice;
    Integer totalDeliveryTime;
    Date expectCompleteDate;
    DeliveryStatus deliveryStatus;
    OrderStatus status;

    UUID buyerId;
    UUID sellerId;

    public ContractResponse(UUID id, UUID packageId, String contractCode,
                            String requirement, Integer quantity, Integer contractCancelFee,
                            BigDecimal serviceDeposit, BigDecimal totalPrice,
                            Integer totalDeliveryTime, Date expectCompleteDate,
                            DeliveryStatus deliveryStatus, OrderStatus status,
                            UUID buyerId, UUID sellerId) {
        this.id = id;
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
        this.status = status;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }
}
