package org.profin.accountservice.service;

import lombok.extern.slf4j.Slf4j;
import org.profin.accountservice.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    @Autowired
    public TransactionProducer(KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(TransactionDTO transaction) {
        kafkaTemplate.send("transactions", transaction.getId().toString(), transaction);
        log.info("Producer sent message");
    }
}
