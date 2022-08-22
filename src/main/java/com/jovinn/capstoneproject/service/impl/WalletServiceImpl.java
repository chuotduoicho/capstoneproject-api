package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.config.payment.PaypalPaymentIntent;
import com.jovinn.capstoneproject.config.payment.PaypalPaymentMethod;
import com.jovinn.capstoneproject.dto.client.request.WalletRequest;
import com.jovinn.capstoneproject.dto.client.request.WithdrawAddressRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.TransactionResponse;
import com.jovinn.capstoneproject.dto.client.response.WalletResponse;
import com.jovinn.capstoneproject.enumerable.PaymentConfirmStatus;
import com.jovinn.capstoneproject.enumerable.TransactionType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.BadRequestException;
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
import com.jovinn.capstoneproject.util.WebConstant;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người dùng"));
        Wallet wallet = walletRepository.findWalletByUserId(user.getId());
        if (user.getId().equals(currentUser.getId())) {
            if(user.getIsEnabled().equals(Boolean.TRUE)) {
                try {
                    Payment payment = paymentService.createPayment(request.getCharge(), request.getCurrency(),
                            PaypalPaymentMethod.PAYPAL, PaypalPaymentIntent.SALE, "BUY " + request.getCharge() + " JCOIN",
                            "jovinnserver.site/api/v1/payment/cancel", WebConstant.DOMAIN + "/buyerhome/manageWallet");
                    System.out.println(payment.toJSON());
                    wallet.setConfirmPayStatus(PaymentConfirmStatus.READY);
                    walletRepository.save(wallet);
                    for(Links link:payment.getLinks()) {
                        if (link.getRel().equals("approval_url")) {
                            return link.getHref();
                        }
                    }
                } catch (PayPalRESTException e) {
                    e.printStackTrace();
                }
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn cần xác thực tài khoản trước khi giao dịch JCOIN");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public WalletResponse getWallet(UserPrincipal currentUser) {
        Wallet wallet = findWallet(currentUser);
        if (wallet.getUser().getId().equals(currentUser.getId())) {
            return new WalletResponse(wallet.getId(), wallet.getIncome(), wallet.getWithdraw(),
                    wallet.getWithdrawAddress(), wallet.getTransactions());
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

    @Override
    public ApiResponse addWithdrawAddress(WithdrawAddressRequest request, UserPrincipal currentUser) {
        Wallet wallet = findWallet(currentUser);
        if(wallet.getWithdrawAddress() != null) {
            wallet.setWithdrawAddress(request.getWithdrawAddress());
            walletRepository.save(wallet);
            return new ApiResponse(Boolean.TRUE, "Bạn đã thay đổi địa chỉ rút tiền thành công");
        } else {
            wallet.setWithdrawAddress(request.getWithdrawAddress());
            walletRepository.save(wallet);
            return new ApiResponse(Boolean.TRUE, "Bạn đã thêm địa chỉ rút tiền thành công");
        }
    }

    @Override
    public ApiResponse withdraw(WalletRequest request, UserPrincipal currentUser) {
        Wallet wallet = findWallet(currentUser);
        try {
            if(request.getCharge().compareTo(wallet.getWithdraw()) <= 0) {
                if(wallet.getWithdrawAddress() != null) {
                    String message =  "Đã thực hiện rút $" + request.getCharge() + " bởi "
                            + currentUser.getLastName() + " " + currentUser.getFirstName();

                    wallet.setWithdraw(wallet.getWithdraw().subtract(request.getCharge()));
                    walletRepository.save(wallet);

                    Transaction transaction = new Transaction();
                    transaction.setWallet(wallet);
                    transaction.setAmount(request.getCharge());
                    transaction.setUserId(wallet.getUser().getId());
                    transaction.setDescription(wallet.getWithdrawAddress());
                    transaction.setMethod("paypal");
                    transaction.setIntent("sale");
                    transaction.setPaymentCode("JOV-Withdraw-" + RandomString.make(8));
                    transaction.setCurrency(request.getCurrency());
                    transaction.setType(TransactionType.WITHDRAW);
                    transaction.setMessage(message);
                    transactionRepository.save(transaction);
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn cần tạo địa chỉ rút trước khi thực hiện (On Paypal Account)");
                }
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn chỉ được rút tối đa $ " + wallet.getWithdraw());
            }
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã xảy ra lỗi khi rút tiền");
        }

        return new ApiResponse(Boolean.TRUE, "Đã thực hiện rút tiền thành công, tiền sẽ chuyển về tài khoản trong ngày 30 hàng tháng");
    }

    @Override
    public List<TransactionResponse> getWithdrawRequestList(String year, String month) {
        List<Transaction> payoutRequest = transactionRepository.findAllTransactionWithdraw(TransactionType.WITHDRAW, year, month);
        List<TransactionResponse> responses = new ArrayList<>();
        for(Transaction transaction : payoutRequest) {
            responses.add(new TransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getType(),
                    transaction.getCurrency(), transaction.getMethod(),
                    transaction.getIntent(), transaction.getDescription(),
                    transaction.getPaymentCode(), transaction.getUserId(),
                    transaction.getMessage(), transaction.getWallet().getId(), transaction.getCreateAt()));
        }
        return responses;
    }

    @Override
    public void exportCsvWithdraw(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=withdraw_request_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        List<Transaction> payoutRequest = transactionRepository.findAllTransactionWithdrawRequest(TransactionType.WITHDRAW);
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Email/Phone", "Amount", "Currency code", "Reference ID (optional)",
                "Note to recipient", "Recipient wallet", "Social Feed Privacy (optional)", "Holler URL (deprecated)", "Logo URL (optional)"};
        String[] nameMapping = {"description", "amount", "currency", "userId", "message", "method",};
        csvWriter.writeHeader(csvHeader);

        for (Transaction transaction : payoutRequest) {
            csvWriter.write(transaction, nameMapping);
        }
        csvWriter.close();
    }

    private Wallet findWallet(UserPrincipal currentUser) {
        return walletRepository.findWalletByUserId(currentUser.getId());
    }
}
