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

/**
 * Service class responsible for sending email receipts for transactions.
 * It uses Thymeleaf for template rendering and JavaMailSender for sending emails.
 */
@Service
@Slf4j  // Добавьте эту аннотацию
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    /**
     * Sends an email containing transaction details to the specified recipient.
     *
     * @param to The email address to which the receipt should be sent.
     * @param transaction The transaction details used to populate the email template.
     *
     * @throws RuntimeException if an error occurs while attempting to send the email.
     */
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

    /**
     * Determines the email subject line based on the transaction type.
     *
     * @param transaction The transaction from which the subject is derived.
     * @return A String describing the email subject, tailored to the transaction type.
     */
    private String getSubjectByTransactionType(TransactionDTO transaction) {
        return switch (transaction.getTransactionType()) {
            case DEPOSIT -> "Deposit Receipt - Transaction #" + transaction.getId();
            case WITHDRAWAL -> "Withdrawal Receipt - Transaction #" + transaction.getId();
            case TRANSFER -> "Transfer Receipt - Transaction #" + transaction.getId();
        };
    }
}