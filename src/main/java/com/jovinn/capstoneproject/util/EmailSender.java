package com.jovinn.capstoneproject.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;
    public String sendVerify(String link) {
        return  "<p>Hello,</p>"
                + "<p>You have requested to register.</p>"
                + "<p>Click the link below to verify your email:</p>"
                + "<p><a href=\"" + link + "\">Verify my email</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do have an account with this email, "
                + "or you have not made the request.</p>";
    }
    public String sendReset(String link) {
        return "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
    }
    //===================== SEND NOTI FOR B&S when Buyer create new Contract =======================
    public String sendNotiContractSeller(String lastName, String link, BigDecimal totalPrice, Integer quantity) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>Happy to have new ORDER</p>"
                + "<p>Click the link to view details this ORDER</p>"
                + "<p><a href=\"" + link + "\">LINK ACCESS ORDER</a></p>"
                + "<p>Total-Price: " + totalPrice + "$<p>"
                + "<p>Quantity: " + quantity + "<p>"
                + "<br>";
    }
    public String sendNotiContractBuyer(String lastName, String link, BigDecimal totalPrice, Integer quantity) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>Happy to have new ORDER</p>"
                + "<p>Click the link to view details this ORDER</p>"
                + "<p><a href=\"" + link + "\">View ORDER</a></p>"
                + "<p>Total-Price: " + totalPrice + "$<p>"
                + "<p>Quantity: " + quantity + "<p>"
                + "<br>";
    }

    //================================ ACCEPT CONTRACT BY SELLER =======================
    public String sendNotiAcceptToSeller(String lastName, String link, String contractCode,
                                         BigDecimal totalPrice, Integer quantity, Date expectDeliveryTime) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>Ready for working " + contractCode + "</p>"
                + "<p>Total-Price: " + totalPrice + "$<p>"
                + "<p>Quantity: " + quantity + "<p>"
                + "<p>Remember you need delivery before " + expectDeliveryTime + "<p>"
                + "<p>Click the link to view details this ORDER</p>"
                + "<p><a href=\"" + link + "\">View ORDER</a></p>"
                + "<br>";
    }
    public String sendNotiAcceptToBuyer(String lastName, String brandName, String link, String contractCode,
                                        BigDecimal totalPrice, Integer quantity) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>Happy to you " + brandName + " ready for working " + contractCode + "</p>"
                + "<p>Total-Price: " + totalPrice + "$<p>"
                + "<p>Quantity: " + quantity + "<p>"
                + "<p>Click the link to view details this ORDER</p>"
                + "<p><a href=\"" + link + "\">View ORDER</a></p>"
                + "<br>";
    }
    // ============================ REJECT CONTRACT BY SELLER ==============================
    public String sendNotiRejectToSeller(String lastName, String link, String contractCode) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>Feeling bad to you to cancel ORDER " + contractCode + "</p>"
                + "<p>Click the link to view details this ORDER</p>"
                + "<p><a href=\"" + link + "\">View ORDER</a></p>"
                + "<br>";
    }
    public String sendNotiRejectToBuyer(String lastName, String brandName,
                                        String link, String contractCode, BigDecimal totalPrice) {
        return "<p>Hello," + lastName + "</p>"
                + "<p>So sorry sir, "+ brandName + " was cancel the ORDER " + contractCode + "</p>"
                + "<p>And we will refund: " + totalPrice + " for you<p>"
                + "<p>Can find more service box in Jovinn</p>"
                + "<p><a href=\"" + link + "\">JOVINN PAGE</a></p>"
                + "<br>";
    }

    public String sendNotiComplete(String lastName, String link, String contractCode, BigDecimal totalPrice) {
        return "<p>Hello," + lastName + "</p>"
                + "Happy to Complete your contract -" + contractCode + "- with 90% of " + totalPrice + "$ "
                + "<p>Can find more service box in Jovinn</p>"
                + "<p><a href=\"" + link + "\">JOVINN THANK FULL</a></p>"
                + "<br>";
    }
    public void sendEmailVerify(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to verify your email";
        String content = sendVerify(link);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailResetPassword(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";
        String content = sendReset(link);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiContractSeller(String recipientEmail, String lastName,
                                            String link, BigDecimal totalPrice, Integer quantity) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiContractSeller(lastName, link, totalPrice, quantity);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiContractBuyer(String recipientEmail, String lastName,
                                           String link, BigDecimal totalPrice, Integer quantity) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiContractBuyer(lastName, link, totalPrice, quantity);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiAcceptContractToSeller(String recipientEmail, String lastName,
                                                    String link, String contractCode,
                                                    BigDecimal totalPrice, Integer quantity, Date expectDeliveryTime) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiAcceptToSeller(lastName, link, contractCode,
                                                totalPrice, quantity, expectDeliveryTime);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiAcceptContractToBuyer(String recipientEmail, String lastName, String brandName,
                                                   String link, String contractCode,
                                                   BigDecimal totalPrice, Integer quantity) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiAcceptToBuyer(lastName, brandName, link,
                                               contractCode, totalPrice, quantity);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiRejectContractToSeller(String recipientEmail, String lastName,
                                                    String link, String contractCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiRejectToSeller(lastName, link, contractCode);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailNotiRejectContractToBuyer(String recipientEmail, String lastName, String brandName,
                                                   String link, String contractCode, BigDecimal totalPrice) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiRejectToBuyer(lastName, brandName, link, contractCode, totalPrice);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


    public void sendEmailCompleteContract(String recipientEmail, String lastName,
                                                   String link, String contractCode, BigDecimal totalPrice) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("duc24600@gmail.com", "Jovinn support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link come to new order";
        String content = sendNotiComplete(lastName, link, contractCode, totalPrice);

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
