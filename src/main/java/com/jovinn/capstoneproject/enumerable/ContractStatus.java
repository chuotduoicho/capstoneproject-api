package com.jovinn.capstoneproject.enumerable;

public enum ContractStatus {
    //When Buyer sending request contract for Seller -> ACTIVE
    //When Buyer accept delivery from Seller -> COMPLETE
    //When Seller reject request -> CANCEL
    PROCESSING, COMPLETE, CANCEL
}
