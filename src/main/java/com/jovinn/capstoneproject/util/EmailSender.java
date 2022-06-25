package com.jovinn.capstoneproject.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

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
}
