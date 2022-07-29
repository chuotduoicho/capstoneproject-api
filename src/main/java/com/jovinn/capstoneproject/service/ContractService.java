package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.AdminViewContractsResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.dto.response.CountContractResponse;
import com.jovinn.capstoneproject.dto.response.CountTotalRevenueResponse;
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
    Contract getContractById(UUID id, UserPrincipal currentUser);
    CountTotalRevenueResponse getTotalRevenue();
    CountContractResponse countTotalContractByCatId(UUID catId);
    List<AdminViewContractsResponse> getContractsByCategoryId(UUID catId);
    PageResponse<Contract> getContractByStatus(ContractStatus status, UserPrincipal currentUser, int page, int size, String sortBy, String sortDir);
    PageResponse<Contract> getOrders(UserPrincipal currentUser, int page, int size, String sortBy, String sortDir);
    PageResponse<Contract> getContracts(UserPrincipal currentUser, int page, int size, String sortBy, String sortDir);
}
