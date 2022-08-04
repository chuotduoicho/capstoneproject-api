package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.ContractRequest;
import com.jovinn.capstoneproject.dto.adminsite.AdminViewContractsResponse;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ContractResponse;
import com.jovinn.capstoneproject.dto.adminsite.CountContractResponse;
import com.jovinn.capstoneproject.dto.adminsite.CountTotalRevenueResponse;
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
    ApiResponse acceptDeliveryForMilestone(UUID contractId, UUID milestoneId, UserPrincipal currentUser);
    ContractResponse createContractFromSellerOffer(UUID offerRequestId, UserPrincipal currentUser);
    ContractResponse createContractFromSellerApply(UUID postRequestId, UUID sellerId, UserPrincipal currentUser);
    ApiResponse flagNotAcceptDelivery(UUID contractId, UserPrincipal currentUser);
    Contract getContractById(UUID id, UserPrincipal currentUser);
    CountTotalRevenueResponse getTotalRevenue();
    CountContractResponse countTotalContractByCatId(UUID catId);
    List<AdminViewContractsResponse> getContractsByCategoryId(UUID catId);
    List<Contract> getContractByStatus(ContractStatus status, UserPrincipal currentUser);
    List<Contract> getOrders(UserPrincipal currentUser);
    List<Contract> getContracts(UserPrincipal currentUser);
    ApiResponse autoCheckCompleteContract();
}
