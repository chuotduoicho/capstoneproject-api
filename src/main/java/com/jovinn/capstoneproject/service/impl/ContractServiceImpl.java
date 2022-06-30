package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.OrderStatus;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ContractService;
import com.jovinn.capstoneproject.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
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
    @Override
    public ContractResponse createContract(ContractRequest request, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "Buyer not found ", currentUser.getId()));
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found ", request.getSellerId()));
        //PackageOptional packageOptional = packageOptionalRepository.findPackageOptionalByPackId(pack.getId())
        //.orElseThrow(() -> new ResourceNotFoundException("PackageOptional", "PackageOptional not found ", pack.getId()));
        Package pack = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package", "Package not found ", request.getPackageId()));
        Wallet walletBuyer = walletRepository.findWalletByUserId(currentUser.getId());
        String contractCode = getRandomContractNumber();

        //double extraPrice = packageOptional.getOptionPrice();
        double subTotal = pack.getPrice() * request.getQuantity(); //10
        double serviceDeposit = subTotal * 10/100; //1
        double serviceFee = subTotal * 5/100; //0.5
        double totalPrice = pack.getPrice() * request.getQuantity(); //10
        int totalDeliveryTime = pack.getDeliveryTime();

        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)) {
            if (walletBuyer.getWithdraw() >= totalPrice) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() - totalPrice); //50 - 10 = 40
                walletRepository.save(walletBuyer);

                Contract contract = new Contract(request.getPackageId(), contractCode,
                        request.getRequirement(), request.getQuantity(), serviceFee, subTotal, serviceDeposit, totalPrice,
                        totalDeliveryTime, request.getExpectCompleteDate(),
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
                        newContract.getQuantity(), newContract.getServiceFee(),
                        newContract.getSubTotal(), newContract.getServiceDeposit(), newContract.getTotalPrice(),
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
    public ContractResponse updateStatusAcceptFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", currentUser.getId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found", id));
        Wallet walletSeller = walletRepository.findWalletByUserId(currentUser.getId());

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
                    walletSeller.setWithdraw(walletSeller.getWithdraw() - serviceDeposit);// 50 - 1 = 49
                    contract.setStatus(OrderStatus.ACTIVE);
                    contract.setDeliveryStatus(DeliveryStatus.PROCESSING);
                    contract.setExpectCompleteDate(request.getExpectCompleteDate());
                    contract.setUpdatedAt(new Date());
                    contract.setSeller(seller);
                    //Khi accept order ngày nào thì từ ngày hiện tại + totalDeliveryTime ra ngày expectmpleteDate tren front-end
                    contract.setExpectCompleteDate(request.getExpectCompleteDate());

                    String linkOrder = "http://localhost:3000/dashboard/order/" + contract.getId();

                    try {
                        emailSender.sendEmailNotiAcceptContractToSeller(seller.getUser().getEmail(),
                                seller.getUser().getLastName(), linkOrder, contract.getContractCode(),
                                contract.getSubTotal(), contract.getQuantity(), contract.getExpectCompleteDate());

                        emailSender.sendEmailNotiAcceptContractToBuyer(contract.getBuyer().getUser().getEmail(),
                                contract.getBuyer().getUser().getLastName(), seller.getBrandName(), linkOrder,
                                contract.getContractCode(), contract.getTotalPrice(), contract.getQuantity());
                    } catch (UnsupportedEncodingException | MessagingException exception) {
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi khi gửi thông báo tới email của bạn");
                    }

                    Contract update = contractRepository.save(contract);
                    return new ContractResponse(update.getId(), update.getPackageId(),
                            update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getServiceFee(),
                            update.getSubTotal(), update.getServiceDeposit(), update.getTotalPrice(),
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
    //Buyer = 40
    //Seller = 49 + 1 + 9 = 59
    //REJECT ORDER BY SELLER => OrderStatus.CANCEL && DeliveryStatus.REJECT
    @Override
    public ContractResponse updateStatusRejectFromSeller(UUID id, ContractRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", request.getSellerId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found", id));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());

        double buyerReceiveWhenProcessing = contract.getTotalPrice() + (contract.getServiceDeposit() * 90/100);
        double sellerReceiveWhenProcessing = contract.getServiceDeposit() - (contract.getServiceDeposit() * 90/100);

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + buyerReceiveWhenProcessing);//40 + 10 + 0.9 = 50.9
                walletSeller.setWithdraw(walletSeller.getWithdraw());//49
            } else {
                //Refund price for buyer not including serviceFee
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + contract.getTotalPrice());//40 + 10 = 50
                contract.setStatus(OrderStatus.CANCEL);
                contract.setDeliveryStatus(DeliveryStatus.REJECT);
                contract.setUpdatedAt(new Date());
                contract.setSeller(seller);

                walletRepository.save(walletBuyer);
                walletRepository.save(walletSeller);

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
                        update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getServiceFee(),
                        update.getSubTotal(), update.getServiceDeposit(), update.getTotalPrice(),
                        update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                        DeliveryStatus.PROCESSING, OrderStatus.ACTIVE,
                        update.getBuyer().getUser().getId(),
                        update.getSeller().getUser().getId());
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse updateStatusRejectFromBuyer(UUID id, ContractRequest request, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "Buyer not found ", currentUser.getId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found", id));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());

        double buyerReceiveAfterReject = contract.getTotalPrice() - contract.getServiceDeposit() - contract.getServiceFee();
        double sellerReceiveAfterReject = contract.getServiceDeposit() + (contract.getServiceDeposit() * 90/100);

        if (contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + buyerReceiveAfterReject);//40 + 10 - 1 - 0.5 = 48.5
                walletSeller.setWithdraw(walletSeller.getWithdraw() + sellerReceiveAfterReject);//49 + 1 + 0.9 = 50.9
            } else {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw() + contract.getSubTotal() - contract.getServiceFee()); //40 + 9.5 = 49.5
                contract.setStatus(OrderStatus.CANCEL);
                contract.setDeliveryStatus(DeliveryStatus.REJECT);
                contract.setUpdatedAt(new Date());
                contract.setBuyer(buyer);
                //Khi accept order ngày nào thì từ ngày hiện tại + totalDeliveryTime ra ngày expectmpleteDate tren front-end
                contract.setExpectCompleteDate(request.getExpectCompleteDate());
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
                        update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getServiceFee(),
                        update.getSubTotal(), update.getServiceDeposit(), update.getTotalPrice(),
                        update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                        DeliveryStatus.PROCESSING, OrderStatus.ACTIVE,
                        update.getBuyer().getUser().getId(),
                        update.getSeller().getUser().getId());
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
