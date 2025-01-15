package org.profin.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.profin.dto.ProceededTransactionDTO;
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
 * Configuration class for setting up Kafka producer properties.
 * This class defines beans necessary for configuring Kafka producer in a Spring application.
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Creates a producer factory with the necessary configuration properties.
     * This method configures essential producer properties such as bootstrap servers,
     * key and value serializers.
     *
     * @return ProducerFactory<String, ProceededTransactionDTO> Kafka producer factory.
     */
    @Bean
    public ProducerFactory<String, ProceededTransactionDTO> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        // Set Kafka bootstrap server address
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Define the serializer for message keys (String serializer)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Define the serializer for message values (Json serializer)
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Creates a KafkaTemplate that will be used for sending messages to Kafka topics.
     * The KafkaTemplate uses the producer factory to create Kafka producers.
     *
     * @return KafkaTemplate<String, ProceededTransactionDTO> Kafka template for sending messages.
     */
    @Bean
    public KafkaTemplate<String, ProceededTransactionDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
