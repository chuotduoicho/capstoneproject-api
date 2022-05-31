package com.jovinn.capstoneproject.model;

import com.jovinn.capstoneproject.Enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.Enumerable.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID buyer_id;
    UUID seller_id;
    UUID package_id;
    float total_price;
    @Temporal(TemporalType.TIMESTAMP)
    Time total_delivery_time;
    String requirement;
    @Enumerated(EnumType.STRING)
    DeliveryStatus delivery_status;
    @Enumerated(EnumType.STRING)
    OrderStatus status;

}
