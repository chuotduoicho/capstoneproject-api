package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryHaveMilestoneRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryNotMilestoneRequest;
import com.jovinn.capstoneproject.dto.request.RatingRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryHaveMilestoneResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryNotMilestoneResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ContractService;
import com.jovinn.capstoneproject.service.DeliveryService;
import com.jovinn.capstoneproject.service.RatingService;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contract")
@CrossOrigin(origins = "*")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private RatingService ratingService;
    @GetMapping("/details/{id}")
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

    @PostMapping("/{offerRequestId}")
    public ResponseEntity<ContractResponse> createContractFromOffer(@PathVariable("offerRequestId") UUID offerRequestId,
                                                                    @CurrentUser UserPrincipal currentUser) {
        ContractResponse response = contractService.createContractFromSellerOffer(offerRequestId, currentUser);
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

    @PutMapping("/delivery-accept-milestone/{contractId}/{milestoneId}")
    public ResponseEntity<ApiResponse> acceptDeliveryContractFromBuyer(@PathVariable("contractId") UUID contractId,
                                                                       @PathVariable("milestoneId") UUID milestoneId,
                                                                       @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = contractService.acceptDeliveryForMilestone(contractId, milestoneId,  currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{status}")
    public ResponseEntity<PageResponse<Contract>> getContractByStatus(@PathVariable ContractStatus status,
                                                              @CurrentUser UserPrincipal currentUser,
                                                              @RequestParam(name = "page", required = false,
                                                                      defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                              @RequestParam(name = "size", required = false,
                                                                      defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                                              @RequestParam(value = "sortBy",
                                                                      defaultValue = WebConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                                              @RequestParam(value = "sortDir",
                                                                      defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<Contract> response = contractService.getContractByStatus(status, currentUser, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list-order")
    public ResponseEntity<PageResponse<Contract>> getOrders(@CurrentUser UserPrincipal currentUser,
                                    @RequestParam(name = "page", required = false,
                                            defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                    @RequestParam(name = "size", required = false,
                                            defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                    @RequestParam(value = "sortBy",
                                            defaultValue = WebConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir",
                                            defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        PageResponse<Contract> response = contractService.getOrders(currentUser, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list-contract")
    public ResponseEntity<PageResponse<Contract>> getContracts(@CurrentUser UserPrincipal currentUser,
                                   @RequestParam(name = "page", required = false,
                                           defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                   @RequestParam(name = "size", required = false,
                                           defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                   @RequestParam(value = "sortBy",
                                           defaultValue = WebConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                   @RequestParam(value = "sortDir",
                                           defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<Contract> response = contractService.getContracts(currentUser, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/rating/{contractId}")
    public ResponseEntity<ApiResponse> ratingSellerFromBuyer(@PathVariable("contractId") UUID contractId,
                                                    @Valid @RequestBody RatingRequest request,
                                                    @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = ratingService.ratingSeller(contractId, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/rating/{contractId}")
    public ResponseEntity<PageResponse<Rating>> getRatingsForContract(@PathVariable("contractId") UUID contractId,
                                                                      @RequestParam(name = "page", required = false,
                                                                              defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                                      @RequestParam(name = "size", required = false,
                                                                              defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size){
        PageResponse<Rating> response = ratingService.getRatingsForContract(contractId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
