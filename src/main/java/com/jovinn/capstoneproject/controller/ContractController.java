package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryHaveMilestoneRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryNotMilestoneRequest;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryHaveMilestoneResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryNotMilestoneResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ContractService;
import com.jovinn.capstoneproject.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contract")
@CrossOrigin(origins = "*")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private DeliveryService deliveryService;
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable UUID id, @CurrentUser UserPrincipal currentUser) {
        Contract response = contractService.getContractById(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<ContractResponse> createContractFromBuyer(@Valid @RequestBody ContractRequest request,
                                                                    @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.createContract(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{postRequestId}")
    public ResponseEntity<ContractResponse> createContractFromOffer(@PathVariable("postRequestId") UUID postRequestId,
                                                                    @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.createContractFromSellerOffer(postRequestId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{postRequestId}/{sellerId}")
    public ResponseEntity<ContractResponse> createContractFromApply(@PathVariable("postRequestId") UUID postRequestId,
                                                                    @PathVariable("sellerId") UUID sellerId,
                                                                    @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.createContractFromSellerApply(postRequestId, sellerId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/seller/accept/{id}")
    public ResponseEntity<ContractResponse> acceptContractFromSeller(@PathVariable("id") UUID id,
                                                                     @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.updateStatusAcceptFromSeller(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/seller/reject/{id}")
    public ResponseEntity<ContractResponse> rejectContractFromSeller(@PathVariable("id") UUID id,
                                                                     @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.updateStatusRejectFromSeller(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/buyer/reject/{id}")
    public ResponseEntity<ContractResponse> rejectContractFromBuyer(@PathVariable("id") UUID id,
                                                                     @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.updateStatusCancelFromBuyer(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/delivery/{id}")
    public ResponseEntity<DeliveryNotMilestoneResponse> deliveryBySeller(@PathVariable("id") UUID id,
                                                                         @Valid @RequestBody DeliveryNotMilestoneRequest request,
                                                                         @CurrentUser UserPrincipal currentUser) {
        DeliveryNotMilestoneResponse response = deliveryService.createDelivery(id, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/delivery-for-milestone/{id}")
    public ResponseEntity<DeliveryHaveMilestoneResponse> deliveryBySeller(@PathVariable("id") UUID id,
                                                                          @Valid @RequestBody DeliveryHaveMilestoneRequest request,
                                                                          @CurrentUser UserPrincipal currentUser) {
        DeliveryHaveMilestoneResponse response = deliveryService.createDeliveryMilestone(id, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<DeliveryNotMilestoneResponse> updateDelivery(@PathVariable("id") UUID id,
                                                                       @Valid @RequestBody DeliveryNotMilestoneRequest request,
                                                                       @CurrentUser UserPrincipal currentUser) {
        DeliveryNotMilestoneResponse response = deliveryService.update(id, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/delivery-accept/{id}")
    public ResponseEntity<ContractResponse> acceptDeliveryContractFromBuyer(@PathVariable("id") UUID id,
                                                                       @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.updateStatusAcceptDeliveryFromBuyer(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<Contract>> getContractByStatus(@PathVariable ContractStatus status,
                                                              @CurrentUser UserPrincipal currentUser) {
        List<Contract> response = contractService.getContractByStatus(status, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list-order")
    public ResponseEntity<List<Contract>> getOrders(@CurrentUser UserPrincipal currentUser) {
        List<Contract> response = contractService.getOrders(currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list-contract")
    public ResponseEntity<List<Contract>> getContracts(@CurrentUser UserPrincipal currentUser) {
        List<Contract> response = contractService.getContracts(currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
