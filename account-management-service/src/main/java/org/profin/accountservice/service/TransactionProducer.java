package org.profin.accountservice.service;

import lombok.extern.slf4j.Slf4j;

import org.profin.accountservice.dto.request.KafkaTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionProducer {

    private final KafkaTemplate<String, KafkaTransaction> kafkaTemplate;

    @Autowired
    public TransactionProducer(KafkaTemplate<String, KafkaTransaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    //всё таки твой продюсер уже в transactions.processed отправляет
    public void sendTransaction(KafkaTransaction transaction) {
        kafkaTemplate.send("transactions.processed", transaction);
        log.info("Producer sent message");
    }

}
