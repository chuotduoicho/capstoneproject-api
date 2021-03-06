package com.jovinn.capstoneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovinn.capstoneproject.enumerable.OfferRequestStatus;
import com.jovinn.capstoneproject.enumerable.OfferType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "jovinn_server")
public class OfferRequest extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    UUID id;
    String descriptionBio;
    Integer totalDeliveryTime;
    Integer cancelFee;
    BigDecimal offerPrice;
    @Enumerated(EnumType.STRING)
    OfferType offerType;

    @Enumerated(EnumType.STRING)
    OfferRequestStatus offerRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "postRequestId", referencedColumnName = "id")
    PostRequest postRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "sellerId", referencedColumnName = "id")
    Seller seller;
}
