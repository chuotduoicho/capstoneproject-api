package com.jovinn.capstoneproject.service.schedule;

import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Delivery;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.util.DateDelivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private DateDelivery dateDelivery;
    @Autowired
    private SellerRepository sellerRepository;

    @Scheduled(cron = "0 0 * * * MON-SAT")
    void autoCompleteContract() throws InterruptedException {
        List<Contract> contracts = contractRepository.findAllByContractStatus(ContractStatus.PROCESSING);
        List<Contract> list = new ArrayList<>();
        for(Contract contract : contracts) {
            Delivery delivery = deliveryRepository.findByContractId(contract.getId());
            if(delivery != null) {
                Date autoCompleteExpectDate = dateDelivery.expectDate(delivery.getCreateAt().getDay(), 3);
                if(contract.getDeliveryStatus().equals(DeliveryStatus.SENDING) &&
                        autoCompleteExpectDate.compareTo(new Date()) > 0 && contract.getFlag().equals(Boolean.FALSE)) {
                    contract.setContractStatus(ContractStatus.COMPLETE);
                    contractRepository.save(contract);
                    list.add(contract);
                }
            }
        }
        log.info("Đã thay đổi trạng thái của " + list.size() + " hợp đồng");
    }

    @Scheduled(cron = "0 0 15 * * *")
    void autoUpdateRankSeller() throws InterruptedException {
        String[]  ranks = {"BEGINNER", "ADVANCED"};
        for(String rank : ranks) {
            List<Seller> sellersByRank = sellerRepository.findAllByRankSeller(RankSeller.valueOf(rank));
            switch (rank) {
                case "BEGINNER":
                    for(Seller seller : sellersByRank) {
                        YearMonth validMonthUpdateFromBeginner
                                = YearMonth.of(seller.getUser().getJoinSellingAt().getYear(), seller.getUser().getJoinSellingAt().getMonth());
                        YearMonth validMonthToUpdateForBeginner
                                = validMonthUpdateFromBeginner.plusMonths(1);
                        if(seller.getTotalOrderFinish() >= 10 &&
                                seller.getUser().getWallet().getIncome().compareTo(new BigDecimal(100)) >=0 &&
                                validMonthUpdateFromBeginner.compareTo(validMonthToUpdateForBeginner) >= 0) {
                            seller.setRankSeller(RankSeller.ADVANCED);
                            sellerRepository.save(seller);
                            log.info("Đã có " + sellersByRank.size() +
                                    " người bán được cập nhật cấp độ từ BEGINNER lên ADVANCE vào: " + new Date());
                        }
                        log.info("Không có" +
                                 " người bán nào được cập nhật cấp độ từ BEGINNER lên ADVANCE vào: " + new Date());
                    }
                    break;
                case "ADVANCED":
                    for(Seller seller : sellersByRank) {
                        YearMonth validMonthUpdateFromAdvance
                                = YearMonth.of(seller.getUser().getJoinSellingAt().getYear(), seller.getUser().getJoinSellingAt().getMonth());
                        YearMonth validMonthToUpdateForAdvance
                                = validMonthUpdateFromAdvance.plusMonths(3);
                        if(seller.getTotalOrderFinish() >= 50 &&
                                seller.getUser().getWallet().getIncome().compareTo(new BigDecimal(5000)) >=0 &&
                                validMonthUpdateFromAdvance.compareTo(validMonthToUpdateForAdvance) >= 0) {
                            seller.setRankSeller(RankSeller.PROFESSIONAL);
                            sellerRepository.save(seller);
                        }
                        log.info("Không có" +
                                 " người bán nào được cập nhật cấp độ từ ADVANCE lên PROFESSIONAL vào: " + new Date());
                    }
                    break;
            }
        }
    }
}
