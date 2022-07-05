package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface ContractService {
    ContractResponse createContract(ContractRequest request, UserPrincipal currentUser);
    ContractResponse updateStatusAcceptFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser);
    ContractResponse updateStatusRejectFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser);
    ContractResponse updateStatusCancelFromBuyer(UUID id, ContractRequest request, UserPrincipal currentUser);
    ContractResponse updateStatusAcceptDeliveryFromBuyer(UUID id, ContractRequest request, UserPrincipal currentUser);
    Contract getContractById(UUID id);
}
