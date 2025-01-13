package org.profin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.dto.PaymentStatus;
import org.profin.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionListner {

    private final EmailService emailService;

    //dev dev test
//     !!!!! Received message:
//     {"id":2,"userId":1,"recipientId":2,"idSenderAccount":1,"idRecipientAccount":2,"transactionType":"TRANSFER","paymentStatus":"PENDING","amount":100.0}
//    @KafkaListener(topics = "transactions.pending", groupId = "account-management-service-group")
//    public void handlePendingTransaction(String message) {
//        log.info("!!!!! Received message: {}", message);
//    }
//
//
    @KafkaListener(topics = "transactions.notifications", groupId = "notification-service-group")
    public void handleTransaction(TransactionDTO transaction) {
        log.info("Received transaction: {}", transaction);
        try {
            log.info("Sending email for transaction: {}", transaction.getId());
            emailService.sendTransactionReceipt("vozhov.artem1@gmail.com",transaction);
    }catch (Exception e){
        log.error("Failed to send email for transaction: {}", transaction.getId(), e);
        throw new RuntimeException("Failed to send email", e);
        }
    }
}
