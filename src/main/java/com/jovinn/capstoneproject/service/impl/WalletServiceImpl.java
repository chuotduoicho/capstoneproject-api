package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.config.payment.PaypalPaymentIntent;
import com.jovinn.capstoneproject.config.payment.PaypalPaymentMethod;
import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.TransactionResponse;
import com.jovinn.capstoneproject.enumerable.PaymentConfirmStatus;
import com.jovinn.capstoneproject.enumerable.TransactionType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Transaction;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.WalletService;
import com.jovinn.capstoneproject.service.payment.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public String buyJCoin(WalletRequest request, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User not found "));
        Wallet wallet = walletRepository.findWalletByUserId(user.getId());
        if (user.getId().equals(currentUser.getId())) {
            if(user.getIsEnabled().equals(Boolean.TRUE)) {
                try {
                    Payment payment = paymentService.createPayment(request.getCharge(), request.getCurrency(),
                            PaypalPaymentMethod.PAYPAL, PaypalPaymentIntent.SALE, "BUY " + request.getCharge() + " JCOIN",
                            "http://localhost:8080/api/v1/payment/cancel",  "http://localhost:8080/api/v1/payment/success");
                    System.out.println(payment.toJSON());
                    wallet.setConfirmPayStatus(PaymentConfirmStatus.READY);
                    walletRepository.save(wallet);
                    for(Links link:payment.getLinks()) {
                        if (link.getRel().equals("approval_url")) {
                            return "redirect:" + link.getHref();
                        }
                    }
                } catch (PayPalRESTException e) {
                    e.printStackTrace();
                }
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "B???n c???n x??c th???c t??i kho???n tr?????c khi giao d???ch JCOIN");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Wallet getWallet(UserPrincipal currentUser) {
        if (currentUser.getId() != null) {
            return walletRepository.findWalletByUserId(currentUser.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public TransactionResponse saveWallet(String paymentId, String payerId, UserPrincipal currentUser) throws IOException {
        Wallet wallet = walletRepository.findWalletByUserId(currentUser.getId());
        if (wallet.getUser().getId().equals(currentUser.getId())
                && wallet.getConfirmPayStatus().equals(PaymentConfirmStatus.READY)) {
            try {
                Payment payment = paymentService.executePayment(paymentId, payerId);
                System.out.println(payment.toJSON());
                if (payment.getState().equals("approved")) {
                    String message =  "PAYMENT SUCCESSFULLY " + payment.getId() + " WITH - "
                            + payment.getTransactions().get(0).getAmount().getCurrency()
                            + payment.getTransactions().get(0).getAmount().getTotal()
                            + " By " + currentUser.getLastName() + " " + currentUser.getFirstName();

                    Transaction transaction = new Transaction();
                    transaction.setWallet(wallet);
                    transaction.setAmount(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal()));
                    transaction.setUserId(wallet.getUser().getId());
                    transaction.setDescription("PAYMENT " + payment.getId());
                    transaction.setMethod(payment.getPayer().getPaymentMethod());
                    transaction.setIntent(payment.getIntent());
                    transaction.setPaymentCode("JOV-" + payment.getId());
                    transaction.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
                    transaction.setType(TransactionType.CHARGE);
                    transaction.setMessage(message);
                    Transaction updatedTransaction = transactionRepository.save(transaction);
//                    OkHttpClient client = new OkHttpClient();
//
//                    Request request = new Request.Builder()
//                            .url("https://api.apilayer.com/exchangerates_data/convert?to=VND&from=USD&amount=" + payment.getTransactions().get(0).getAmount().getTotal())
//                            .addHeader("apikey", "gsEPnORByvJ8ODDXsmYiHAOeZdFYzaEm")
//                            .get().build();
//
//                    Response response = client.newCall(request).execute();
//                    System.out.println(response.body().string());
//
//                    JSONObject jsonObject = new JSONObject(response.body().string());
//                    JSONObject myResponse = jsonObject.getJSONObject("result");
//                    BigDecimal vndCharge = new BigDecimal(myResponse.toString());
                    wallet.setWithdraw(wallet.getWithdraw().add(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal())));
                    wallet.setConfirmPayStatus(PaymentConfirmStatus.VERIFY);
                    Wallet updatedWallet = walletRepository.save(wallet);

                    return new TransactionResponse(updatedTransaction.getId(), updatedWallet.getWithdraw(), updatedTransaction.getType(),
                            updatedTransaction.getCurrency(), updatedTransaction.getMethod(),
                            updatedTransaction.getIntent(), updatedTransaction.getDescription(),
                            updatedTransaction.getPaymentCode(), updatedTransaction.getUserId(),
                            message, updatedTransaction.getWallet().getId(), updatedTransaction.getCreateAt());
                }
            } catch (PayPalRESTException e) {
                System.out.println(e.getMessage());
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to payment");
        throw new UnauthorizedException(apiResponse);
    }

    //Not using now
    @Override
    public TransactionResponse getTransactionWallet(UserPrincipal currentUser) {
        Transaction transaction = transactionRepository.findTransactionByUserId(currentUser.getId());
        if (transaction != null) {
            return new TransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getType(),
                    transaction.getCurrency(), transaction.getMethod(),
                    transaction.getIntent(), transaction.getDescription(),
                    transaction.getPaymentCode(), transaction.getUserId(),
                    transaction.getMessage(), transaction.getWallet().getId(), transaction.getCreateAt());
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
