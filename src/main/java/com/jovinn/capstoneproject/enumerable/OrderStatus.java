package com.jovinn.capstoneproject.enumerable;

public enum OrderStatus {
    //When Buyer sending request contract for Seller -> ACTIVE
    //When Buyer accept delivery from Seller -> COMPLETE
    //When Seller reject request -> CANCEL
    ACTIVE, REORDER, COMPLETE, CANCEL
}
