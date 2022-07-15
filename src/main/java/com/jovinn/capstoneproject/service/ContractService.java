package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface ContractService {
    ContractResponse createContract(ContractRequest request, UserPrincipal currentUser);
    ContractResponse updateStatusAcceptFromSeller(UUID id, UserPrincipal currentUser);
    ContractResponse updateStatusRejectFromSeller(UUID id, UserPrincipal currentUser);
    ContractResponse updateStatusCancelFromBuyer(UUID id, UserPrincipal currentUser);
    ContractResponse updateStatusAcceptDeliveryFromBuyer(UUID id, UserPrincipal currentUser);
    ContractResponse createContractFromSellerOffer(UUID offerRequestId, UserPrincipal currentUser);
    ContractResponse createContractFromSellerApply(UUID postRequestId, UUID sellerId, UserPrincipal currentUser);
    Contract getContractById(UUID id, UserPrincipal currentUser);
    List<Contract> getContractByStatus(ContractStatus status, UserPrincipal currentUser);
    List<Contract> getOrders(UserPrincipal currentUser);
    List<Contract> getContracts(UserPrincipal currentUser);
}
