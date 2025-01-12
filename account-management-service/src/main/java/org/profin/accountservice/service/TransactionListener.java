package org.profin.accountservice.service;

import lombok.extern.slf4j.Slf4j;
import org.profin.accountservice.dto.request.TransactionDTO;
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
//    @KafkaListener(topics = "transactions.pending")
//    public void handlePendingTransaction(
//            @Payload KafkaTransaction transaction,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//            @Header(KafkaHeaders.OFFSET) long offset
//    ) {
//        log.info("Received message: partition={}, offset={}, transaction={}",
//                partition, offset, transaction);
//
//        // Ваша логика обработки
//    }
//@KafkaListener(topics = "transactions.pending")
//public void handlePendingTransaction(ConsumerRecord<String, KafkaTransaction> record) {
//    try {
//        log.info("Received raw record: {}", record);
//        KafkaTransaction transaction = record.value();
//        log.info("Deserialized transaction: {}", transaction);
//        transactionService.processTransaction(transaction);
//    } catch (Exception e) {
//        log.error("Error processing transaction", e);
//    }
////}
//    @KafkaListener(topics = "transactions.pending")
//    public void handlePendingTransaction(ConsumerRecord<String, String> record) {
//        log.info("Received raw message - Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}",
//                record.topic(),
//                record.partition(),
//                record.offset(),
//                record.key(),
//                record.value()
//        );
//    }
    @KafkaListener(topics = "transactions.pending", groupId = "account-management-service-group")
    public void handlePendingTransaction(String message) {
        log.info("!!!!! Received message: {}", message);
    }

//    @KafkaListener(
//            topics = "transactions.pending",
//            groupId = "${spring.kafka.consumer.group-id}"
//    )
//    public void handlePendingTransaction(KafkaTransaction request) {
//        log.info("Received pending transaction: {}", request);
//
//
//        // todo map from dto
////        transactionService.processTransaction(transaction);
//
//
////        kafkaTemplate.send("transactions.processed", transaction);
//    }
//


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