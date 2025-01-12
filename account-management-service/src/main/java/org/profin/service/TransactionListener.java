package org.profin.service;

import lombok.extern.slf4j.Slf4j;
import org.profin.dto.PaymentStatus;
import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TransactionListener {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    @Autowired
    public TransactionListener(TransactionService transactionService, KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.transactionService = transactionService;
        this.kafkaTemplate = kafkaTemplate;
    }


    //dev dev test
//     !!!!! Received message:
//     {"id":2,"userId":1,"recipientId":2,"idSenderAccount":1,"idRecipientAccount":2,"transactionType":"TRANSFER","paymentStatus":"PENDING","amount":100.0}
//    @KafkaListener(topics = "transactions.pending", groupId = "account-management-service-group")
//    public void handlePendingTransaction(String message) {
//        log.info("!!!!! Received message: {}", message);
//    }
//

    @KafkaListener(topics = "transactions.pending", groupId = "account-management-service-group")
    public void handleTransaction(TransactionDTO transaction) {
        try {
            // Валидация пользователя
            //todo make real transaction processing if only validation
            transactionService.processTransaction(transaction);

            // Устанавливаем флаг валидации
//            transaction.setValid(true);


            // Отправка результата обратно в Kafka (в топик validated-transactions)
            transaction.setPaymentStatus(PaymentStatus.COMPLETED);
            kafkaTemplate.send("transactions.processed", transaction);


            log.info("Validated transaction sent back to Kafka");


        } catch (ValidationException ex) {
            // Логируем ошибку валидации
//            log.error("Validation failed for transaction {}: {}", transaction.getId(), ex.getMessage());

            // Устанавливаем флаг валидности в false
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что транзакция не прошла валидацию
            transaction.setPaymentStatus(PaymentStatus.FAILED);
            kafkaTemplate.send("transactions.processed", transaction);

            log.info("Transaction is invalid!");


        } catch (Exception ex) {
            // Логируем любые другие непредвиденные ошибки
//            log.error("Error processing transaction {}: {}", transaction.getId(), ex.getMessage(), ex);

            // Устанавливаем флаг валидности в false для всех ошибок
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что произошла ошибка в процессе обработки
            transaction.setPaymentStatus(PaymentStatus.FAILED);
            kafkaTemplate.send("transactions.processed", transaction);
            log.info("Internal error has occurred...");

        }
    }
    //todo этот не совсем работает
//    Received message:
//    {"id":2,"userId":null,"recipientId":null,"idSenderAccount":1,"idRecipientAccount":2,"transactionType":"TRANSFER","paymentStatus":"PENDING","amount":100.0}
    @KafkaListener(topics = "transactions.pending", groupId = "account-management-service-group")
    public void handlePendingTransaction(TransactionDTO transactionDTO) {
        log.info("!!!!! Received message: {}", transactionDTO.toString());
    }

//    @KafkaListener(topics = "transactions", groupId = "account-service")
//    public void handleTransaction(KafkaTransaction transaction) {
//        try {
//            // Валидация пользователя
//            transactionService.processTransaction(transaction);
//
//            // Устанавливаем флаг валидации
////            transaction.setValid(true);
//
//
//            // Отправка результата обратно в Kafka (в топик validated-transactions)
//            kafkaTemplate.send("validated-transactions", transaction);
//
//            log.info("Validated transaction sent back to Kafka");
//
//
//        } catch (ValidationException ex) {
//            // Логируем ошибку валидации
////            log.error("Validation failed for transaction {}: {}", transaction.getId(), ex.getMessage());
//
//            // Устанавливаем флаг валидности в false
////            transaction.setValid(false);
//
//            // Отправляем обратно информацию о том, что транзакция не прошла валидацию
//            kafkaTemplate.send("validated-transactions", transaction);
//
//            log.info("Transaction is invalid!");
//
//
//        } catch (Exception ex) {
//            // Логируем любые другие непредвиденные ошибки
////            log.error("Error processing transaction {}: {}", transaction.getId(), ex.getMessage(), ex);
//
//            // Устанавливаем флаг валидности в false для всех ошибок
////            transaction.setValid(false);
//
//            // Отправляем обратно информацию о том, что произошла ошибка в процессе обработки
//            kafkaTemplate.send("validated-transactions", transaction);
//            log.info("Internal error has occurred...");
//
//        }
//    }
}