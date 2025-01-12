package org.profin.accountservice.service;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    @Autowired
    public TransactionListener(TransactionService transactionService, KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.transactionService = transactionService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "transactions", groupId = "account-service")
    public void handleTransaction(TransactionDTO transaction) {
        try {
            // Валидация пользователя
            transactionService.processTransaction(transaction);

            // Устанавливаем флаг валидации
//            transaction.setValid(true);

            // Отправка результата обратно в Kafka (в топик validated-transactions)
            kafkaTemplate.send("validated-transactions", transaction);

        } catch (ValidationException ex) {
            // Логируем ошибку валидации
//            log.error("Validation failed for transaction {}: {}", transaction.getId(), ex.getMessage());

            // Устанавливаем флаг валидности в false
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что транзакция не прошла валидацию
            kafkaTemplate.send("validated-transactions", transaction);

        } catch (Exception ex) {
            // Логируем любые другие непредвиденные ошибки
//            log.error("Error processing transaction {}: {}", transaction.getId(), ex.getMessage(), ex);

            // Устанавливаем флаг валидности в false для всех ошибок
//            transaction.setValid(false);

            // Отправляем обратно информацию о том, что произошла ошибка в процессе обработки
            kafkaTemplate.send("validated-transactions", transaction);
        }
    }
}