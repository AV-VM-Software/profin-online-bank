package org.profin.service;

import lombok.extern.slf4j.Slf4j;

import org.profin.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    @Autowired
    public TransactionProducer(KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    //всё таки твой продюсер уже в transactions.processed отправляет
    public void sendTransaction(TransactionDTO transactionDTO) {
        kafkaTemplate.send("transactions.processed", transactionDTO);
        log.info("Producer sent message");
    }


    public CompletableFuture<SendResult<String, TransactionDTO>> sendTransactionToKafka(TransactionDTO dto, String topic) {
        return kafkaTemplate.send(topic, dto)
                .thenApply(result -> {
                    log.info("Transaction sent to Kafka: {} with offset: {}",
                            dto.getId(), result.getRecordMetadata().offset());
                    return result;
                })
                .exceptionally(ex -> {
                    log.error("Unable to send transaction {} to Kafka: {}",
                            dto.getId(), ex.getMessage());
                    throw new RuntimeException(ex);
                });
    }


}
