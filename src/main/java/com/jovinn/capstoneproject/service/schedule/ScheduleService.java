package com.jovinn.capstoneproject.service.schedule;

import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Delivery;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.util.DateDelivery;
import com.jovinn.capstoneproject.util.PushNotification;
import com.jovinn.capstoneproject.util.WebConstant;
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
    @Autowired
    private PushNotification pushNotification;

    @Scheduled(cron = "0 0 * * * MON-SAT")
    void autoCompleteContract() throws InterruptedException {
        List<Contract> contracts = contractRepository.findAllByContractStatus(ContractStatus.PROCESSING);
        System.out.println(contracts.size());
        List<Contract> list = new ArrayList<>();
        for(Contract contract : contracts) {
            List<Delivery> deliveries = deliveryRepository.findAllByContractIdOrderByCreateAtDesc(contract.getId());
            if(deliveries.size() != 0) {
                Date autoCompleteExpectDate = dateDelivery.expectDateCompleteAuto(deliveries.get(0).getCreateAt(), 3);
                if(contract.getDeliveryStatus().equals(DeliveryStatus.SENDING) &&
                        autoCompleteExpectDate.compareTo(new Date()) < 0 && contract.getFlag().equals(Boolean.FALSE)) {
                    contract.setContractStatus(ContractStatus.COMPLETE);
                    contractRepository.save(contract);
                    pushNotification.sendNotification(contract.getBuyer().getUser(),
                            WebConstant.DOMAIN + "/buyerhome/manageContract/" + contract.getId(),
                            "Hợp đồng " + contract.getContractCode() + " của bạn đã được hoàn thành tự động");
                    pushNotification.sendNotification(contract.getSeller().getUser(),
                            WebConstant.DOMAIN + "/buyerhome/manageContract/" + contract.getId(),
                            "Hợp đồng " + contract.getContractCode() + " của bạn đã được hoàn thành tự động");
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
                    List<Seller> countUpdateSuccessBeginner = new ArrayList<>();
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
                            countUpdateSuccessBeginner.add(seller);
                        }
                    }
                    log.info("Đã có " + countUpdateSuccessBeginner.size() +
                            " người bán được cập nhật cấp độ từ BEGINNER lên ADVANCE vào: " + new Date());
                    break;
                case "ADVANCED":
                    List<Seller> countUpdateSuccessAdvanced = new ArrayList<>();
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
                            countUpdateSuccessAdvanced.add(seller);
                        }
                    }
                    log.info("Đã có " + countUpdateSuccessAdvanced.size() +
                            " người bán được cập nhật cấp độ từ ADVANCE lên PROFESSIONAL vào: " + new Date());
                    break;
            }
        }
    }
}
