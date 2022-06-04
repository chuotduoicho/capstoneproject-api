package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID buyerId;
    UUID sellerId;
    UUID packageId;
    float totalPrice;
    Integer quantity;
    Integer totalDeliveryTime;
    String requirement;
    @Temporal(TemporalType.DATE)
    Date expectCompleteDate;
    @Enumerated(EnumType.STRING)
    DeliveryStatus deliveryStatus;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
}
