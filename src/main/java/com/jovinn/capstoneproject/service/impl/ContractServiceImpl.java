package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ContractService;
import com.jovinn.capstoneproject.util.DateDelivery;
import com.jovinn.capstoneproject.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.jovinn.capstoneproject.util.GenerateRandomNumber.getRandomContractNumber;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DateDelivery dateDelivery;
    @Override
    public ContractResponse createContract(ContractRequest request, UserPrincipal currentUser) throws RuntimeException {
        Package pack = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Package not found "));
        UUID sellerId = pack.getBox().getSeller().getId();
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found "));
        //PackageOptional packageOptional = packageOptionalRepository.findPackageOptionalByPackId(pack.getId())
        //.orElseThrow(() -> new ResourceNotFoundException("PackageOptional", "PackageOptional not found ", pack.getId()));
        Wallet walletBuyer = walletRepository.findWalletByUserId(currentUser.getId());
        String contractCode = getRandomContractNumber();

        //double extraPrice = packageOptional.getOptionPrice();
        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, pack.getDeliveryTime());
        double totalPrice = pack.getPrice() * request.getQuantity();
        double serviceDeposit = totalPrice * request.getContractCancelFee()/100;
        int totalDeliveryTime = pack.getDeliveryTime();

        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)) {
            if (walletBuyer.getWithdraw() >= totalPrice) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() - totalPrice);
                walletRepository.save(walletBuyer);

                Contract contract = new Contract(request.getPackageId(), contractCode,
                        request.getRequirement(), request.getQuantity(), request.getContractCancelFee(),
                        serviceDeposit, totalPrice, totalDeliveryTime, expectCompleteDate,
                        DeliveryStatus.PENDING, OrderStatus.ACTIVE, request.getType(), buyer, seller);
                Contract newContract = contractRepository.save(contract);

                String linkOrderForSeller = "http://localhost:3000/dashboard/" + seller.getBrandName() + "/order/" + newContract.getId();
                String linkOrderForBuyer = "http://localhost:3000/dashboard/" + currentUser.getId() + "/order/" + newContract.getId();

                try {
                    emailSender.sendEmailNotiContractSeller(seller.getUser().getEmail(),
                            seller.getUser().getLastName(), linkOrderForSeller,
                            totalPrice, request.getQuantity());

                    emailSender.sendEmailNotiContractBuyer(currentUser.getEmail(),
                            currentUser.getLastName(), linkOrderForBuyer,
                            totalPrice, request.getQuantity());
                } catch (UnsupportedEncodingException | MessagingException exception) {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi khi gửi thông báo tới email của bạn");
                }

                return new ContractResponse(newContract.getId(), newContract.getPackageId(),
                        newContract.getContractCode(), newContract.getRequirement(),
                        newContract.getQuantity(), newContract.getContractCancelFee(),
                        newContract.getServiceDeposit(), newContract.getTotalPrice(),
                        newContract.getTotalDeliveryTime(), newContract.getExpectCompleteDate(),
                        DeliveryStatus.PENDING, OrderStatus.ACTIVE,
                        newContract.getBuyer().getUser().getId(),
                        newContract.getSeller().getUser().getId());
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Trong tài khoản hiện không đủ tiền để thanh toán");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    //ACCEPT ORDER BY SELLER => OrderStatus.ACTIVE && DeliveryStatus.PROCESSING
    //When seller accept order
    @Override
    public ContractResponse updateStatusAcceptFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser) throws RuntimeException {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", currentUser.getId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found", id));
        Wallet walletSeller = walletRepository.findWalletByUserId(currentUser.getId());

        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, contract.getTotalDeliveryTime());
        double serviceDeposit = contract.getServiceDeposit();

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if(contract.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn đang trong quá trình thực hiện hợp đồng");
            } else if(contract.getDeliveryStatus().equals(DeliveryStatus.REJECT)
                    || contract.getStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể tiếp tục thực hiện hợp đồng do bạn hoặc người bán từ chối");
            } else {
                if (walletSeller.getWithdraw() >= serviceDeposit) {
                    //Need min withdraw to accept contract
                    walletSeller.setWithdraw(walletSeller.getWithdraw() - serviceDeposit);
                    contract.setStatus(OrderStatus.ACTIVE);
                    contract.setDeliveryStatus(DeliveryStatus.PROCESSING);
                    //Khi accept order ngày nào thì từ ngày hiện tại + totalDeliveryTime ra ngày expectmpleteDate tren front-end
                    contract.setExpectCompleteDate(expectCompleteDate);
                    contract.setUpdatedAt(new Date());
                    Delivery delivery = new Delivery();
                    delivery.setContract(contract);

                    String linkOrder = "http://localhost:3000/dashboard/order/" + contract.getId();

                    try {
                        emailSender.sendEmailNotiAcceptContractToSeller(seller.getUser().getEmail(),
                                seller.getUser().getLastName(), linkOrder, contract.getContractCode(),
                                contract.getTotalPrice(), contract.getQuantity(), expectCompleteDate);

                        emailSender.sendEmailNotiAcceptContractToBuyer(contract.getBuyer().getUser().getEmail(),
                                contract.getBuyer().getUser().getLastName(), seller.getBrandName(), linkOrder,
                                contract.getContractCode(), contract.getTotalPrice(), contract.getQuantity());
                    } catch (UnsupportedEncodingException | MessagingException exception) {
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi khi gửi thông báo tới email của bạn");
                    }

                    Contract update = contractRepository.save(contract);
                    return new ContractResponse(update.getId(), update.getPackageId(),
                            update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getContractCancelFee(),
                            update.getServiceDeposit(), update.getTotalPrice(),
                            update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                            DeliveryStatus.PROCESSING, OrderStatus.ACTIVE,
                            update.getBuyer().getUser().getId(),
                            update.getSeller().getUser().getId());
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Để tham gia hợp đồng bạn cần có một lượng JCoin ít nhất là: " + contract.getServiceDeposit());
                }
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to accept this order");
        throw new UnauthorizedException(apiResponse);
    }

    //REJECT ORDER BY SELLER => OrderStatus.CANCEL && DeliveryStatus.REJECT
    @Override
    public ContractResponse updateStatusRejectFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser) throws RuntimeException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Seller seller = sellerRepository.findById(contract.getSeller().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        double buyerReceiveWhenProcessing = contract.getTotalPrice() + (contract.getServiceDeposit() * 90/100);

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + buyerReceiveWhenProcessing);
            }  else if (contract.getDeliveryStatus().equals(DeliveryStatus.REJECT)
                    || contract.getStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể từ chối do hợp đồng đã kết thúc");
            } else {
                //Refund price for buyer not including serviceFee
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + contract.getTotalPrice());
            }

            contract.setStatus(OrderStatus.CANCEL);
            contract.setDeliveryStatus(DeliveryStatus.REJECT);
            contract.setUpdatedAt(new Date());
            contract.setSeller(seller);

            walletRepository.save(walletBuyer);

            String linkOrder = "http://localhost:3000/dashboard/order" + contract.getId();
            try {
                emailSender.sendEmailNotiRejectContractToSeller(seller.getUser().getEmail(),
                        seller.getUser().getLastName(), linkOrder, contract.getContractCode());

                emailSender.sendEmailNotiRejectContractToBuyer(contract.getBuyer().getUser().getEmail(),
                        contract.getBuyer().getUser().getLastName(), seller.getBrandName(), linkOrder,
                        contract.getContractCode(), contract.getTotalPrice());
            } catch (UnsupportedEncodingException | MessagingException exception) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi thông báo tới email của bạn");
            }

            Contract update = contractRepository.save(contract);
            return new ContractResponse(update.getId(), update.getPackageId(),
                    update.getContractCode(), update.getRequirement(), update.getQuantity(),
                    update.getContractCancelFee(), update.getServiceDeposit(), update.getTotalPrice(),
                    update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                    DeliveryStatus.REJECT, OrderStatus.ACTIVE,
                    update.getBuyer().getUser().getId(),
                    update.getSeller().getUser().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse updateStatusCancelFromBuyer(UUID id, ContractRequest request, UserPrincipal currentUser) throws RuntimeException {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getSeller().getUser().getId());

        double buyerReceiveAfterCancel = contract.getTotalPrice() - contract.getServiceDeposit();
        double sellerReceiveAfterCancel = contract.getServiceDeposit() + contract.getServiceDeposit() * 90/100;

        if (contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + buyerReceiveAfterCancel);
                walletSeller.setWithdraw(walletSeller.getWithdraw() + sellerReceiveAfterCancel);
            } else if (contract.getDeliveryStatus().equals(DeliveryStatus.REJECT)
                    || contract.getStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể từ chối do hợp đồng đã kết thúc");
            } else {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + contract.getTotalPrice());
            }
            contract.setStatus(OrderStatus.CANCEL);
            contract.setDeliveryStatus(DeliveryStatus.REJECT);
            contract.setUpdatedAt(new Date());
            contract.setBuyer(buyer);

            walletRepository.save(walletBuyer);
            walletRepository.save(walletSeller);

            String linkOrder = "http://localhost:3000/dashboard/order" + contract.getId();
            try {
                emailSender.sendEmailNotiRejectContractToSeller(contract.getSeller().getUser().getEmail(),
                        contract.getSeller().getUser().getLastName(), linkOrder, contract.getContractCode());

                emailSender.sendEmailNotiRejectContractToBuyer(contract.getBuyer().getUser().getEmail(),
                        contract.getBuyer().getUser().getLastName(), contract.getSeller().getBrandName(), linkOrder,
                        contract.getContractCode(), contract.getTotalPrice());
            } catch (UnsupportedEncodingException | MessagingException exception) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi thông báo tới email của bạn");
            }

            Contract update = contractRepository.save(contract);
            return new ContractResponse(update.getId(), update.getPackageId(),
                    update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getContractCancelFee(),
                    update.getServiceDeposit(), update.getTotalPrice(),
                    update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                    DeliveryStatus.REJECT, OrderStatus.CANCEL,
                    update.getBuyer().getUser().getId(),
                    update.getSeller().getUser().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Contract getContractById(UUID id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found ", id));
    }
}
