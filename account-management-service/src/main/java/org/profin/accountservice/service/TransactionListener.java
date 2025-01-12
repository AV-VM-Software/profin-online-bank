package org.profin.accountservice.service;

import lombok.extern.slf4j.Slf4j;
import org.profin.accountservice.dto.request.KafkaTransaction;
import org.profin.accountservice.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TransactionListener {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, KafkaTransaction> kafkaTemplate;

    @Autowired
    public TransactionListener(TransactionService transactionService, KafkaTemplate<String, KafkaTransaction> kafkaTemplate) {
        this.transactionService = transactionService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "transactions.pending", groupId = "transaction-service-group")
    public void handlePendingTransaction(KafkaTransaction request) {
        log.info("Received pending transaction: {}", request);


        // todo map from dto
//        transactionService.processTransaction(transaction);


//        kafkaTemplate.send("transactions.processed", transaction);
    }



    @KafkaListener(topics = "transactions", groupId = "account-service")
    public void handleTransaction(KafkaTransaction transaction) {
        try {
            // Валидация пользователя
            transactionService.processTransaction(transaction);

            // Устанавливаем флаг валидации
//            transaction.setValid(true);


            // Отправка результата обратно в Kafka (в топик validated-transactions)
            kafkaTemplate.send("validated-transactions", transaction);

            log.info("Validated transaction sent back to Kafka");


        } catch (ValidationException ex) {
            // Логируем ошибку валидации
//            log.error("Validation failed for transaction {}: {}", transaction.getId(), ex.getMessage());

            // Устанавливаем флаг валидности в false
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что транзакция не прошла валидацию
            kafkaTemplate.send("validated-transactions", transaction);

            log.info("Transaction is invalid!");


        } catch (Exception ex) {
            // Логируем любые другие непредвиденные ошибки
//            log.error("Error processing transaction {}: {}", transaction.getId(), ex.getMessage(), ex);

            // Устанавливаем флаг валидности в false для всех ошибок
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что произошла ошибка в процессе обработки
            kafkaTemplate.send("validated-transactions", transaction);
            log.info("Internal error has occurred...");

        }
    }
}