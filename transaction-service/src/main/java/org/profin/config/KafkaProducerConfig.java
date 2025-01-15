package org.profin.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.profin.dto.TransactionDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
/**
 * Configuration class for setting up Kafka producers in the application.
 * It provides beans for the ProducerFactory and KafkaTemplate to send
 * messages to Kafka topics.
 */
@Configuration
public class KafkaProducerConfig {
    /**
     * Defines the configuration for Kafka producers, including serializer
     * classes and bootstrap server settings.
     *
     * @return a ProducerFactory for creating Kafka producer instances
     */
    @Bean
    public ProducerFactory<String, TransactionDTO> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    /**
     * Creates a KafkaTemplate for publishing messages to Kafka. This template
     * abstracts the underlying producer operations.
     *
     * @return a KafkaTemplate tied to the configured ProducerFactory
     */
    @Bean
    public KafkaTemplate<String, TransactionDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
