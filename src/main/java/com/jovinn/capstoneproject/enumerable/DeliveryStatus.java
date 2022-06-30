package com.jovinn.capstoneproject.enumerable;

public enum DeliveryStatus {
    //When Buyer sending request to Seller -> PENDING
    //WHen Seller accept request -> PROCESSING
    //When Seller delivery -> DELIVERY
    //When Buyer accept delivery -> COMPLETE contract
    //When Seller reject request -> REJECT
    PENDING, REJECT, PROCESSING, DELIVERY, COMPLETE
}
