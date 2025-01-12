package org.profin.accountservice.service;

import lombok.extern.slf4j.Slf4j;

import org.profin.accountservice.dto.request.TransactionDTO;
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
    //всё таки твой продюсер уже в transactions.processed отправляет
    public void sendTransaction(TransactionDTO transactionDTO) {
        kafkaTemplate.send("transactions.processed", transactionDTO);
        log.info("Producer sent message");
    }

}
