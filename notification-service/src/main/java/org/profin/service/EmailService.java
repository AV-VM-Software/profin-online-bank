package org.profin.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.profin.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j  // Добавьте эту аннотацию
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendTransactionReceipt(String to, TransactionDTO transaction) {
        try {
            log.info("Starting to prepare email for transaction: {}", transaction.getId());

            Context context = new Context();
            context.setVariable("transaction", transaction);

            log.info("Processing template");
            String htmlContent = templateEngine.process("receipt", context);
            log.info("Template processed successfully");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(getSubjectByTransactionType(transaction));
            helper.setText(htmlContent, true);
            helper.setFrom(new InternetAddress("vozhov.artem1@gmail.com", "Profin Service"));

            log.info("Sending email to: {}", to);
            mailSender.send(message);
            log.info("Email sent successfully");

        } catch (Exception e) {
            log.error("Failed to send email for transaction: {}", transaction.getId(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String getSubjectByTransactionType(TransactionDTO transaction) {
        return switch (transaction.getTransactionType()) {
            case DEPOSIT -> "Deposit Receipt - Transaction #" + transaction.getId();
            case WITHDRAWAL -> "Withdrawal Receipt - Transaction #" + transaction.getId();
            case TRANSFER -> "Transfer Receipt - Transaction #" + transaction.getId();
        };
    }
}