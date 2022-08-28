package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import com.jovinn.capstoneproject.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus status, UUID buyerId, UUID sellerId);
    List<Contract> findAllByOrderStatusAndBuyerId(OrderStatus status, UUID buyerId);
    List<Contract> findAllByOrderStatusAndSellerId(OrderStatus status, UUID sellerId);
    @Query(value = "select sum(total_price*0.1) as total_revenue from jovinn_server.contract where contract_status = \"COMPLETE\"", nativeQuery = true)
    BigDecimal countTotalRevenue();
    List<Contract> findAllByContractStatusAndBuyerId(ContractStatus status, UUID buyerId);
    List<Contract> findAllByContractStatusAndSellerIdOrBuyerId(ContractStatus status, UUID sellerId, UUID buyerId);
    List<Contract> findAllByContractStatusAndCreateAt(ContractStatus status, Date date);
    @Query(value = "select sum(total_price*0.1) as revenue from jovinn_server.contract where contract_status = \"COMPLETE\" and date(create_at) = date(now())", nativeQuery = true)
    BigDecimal countTotalRevenueToday();
    @Query(value = "select sum(total_price*0.1) as revenue \n" +
            "from jovinn_server.contract \n" +
            "where contract_status = \"COMPLETE\" \n" +
            "and YEAR(create_at) = YEAR(CURRENT_DATE - INTERVAL ?1 MONTH)\n" +
            "AND MONTH(create_at) = MONTH(CURRENT_DATE - INTERVAL ?1 MONTH)", nativeQuery = true)
    BigDecimal countTotalRevenueByMonth(Integer month);
    Long countContractByPostRequest_Category_Id(UUID catId);
    List<Contract> findAllByPostRequest_Category_Id(UUID catId);
    List<Contract> findAllByContractStatus(ContractStatus status);
    Boolean existsContractByPackageIdAndOrderStatusOrContractStatus(UUID packageId, OrderStatus orderStatus, ContractStatus contractStatus);
}
