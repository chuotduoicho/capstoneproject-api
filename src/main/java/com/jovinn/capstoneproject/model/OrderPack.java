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
public class OrderPack extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;
    UUID buyer_id;
    UUID seller_id;
    UUID package_id;
    float total_price;
    Integer quantity;
    Integer total_delivery_time;
    String requirement;
    @Temporal(TemporalType.DATE)
    Date expect_complete_date;
    @Enumerated(EnumType.STRING)
    DeliveryStatus delivery_status;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
}
