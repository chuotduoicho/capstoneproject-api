package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import com.jovinn.capstoneproject.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    Page<Contract> findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus status, UUID buyerId, UUID sellerId, Pageable pageable);
    Page<Contract> findAllByOrderStatusAndBuyerId(OrderStatus status, UUID buyerId, Pageable pageable);
    List<Contract> findAllByOrderStatusAndSellerId(OrderStatus status, UUID sellerId);
    Page<Contract> findAllByContractStatusAndBuyerId(ContractStatus status, UUID buyerId, Pageable pageable);
    Page<Contract> findAllByContractStatusAndSellerIdOrBuyerId(ContractStatus status, UUID sellerId, UUID buyerId, Pageable pageable);

}
