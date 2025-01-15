package org.profin.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up Kafka consumer properties.
 * This class defines beans necessary for configuring Kafka consumer in a Spring application.
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * Creates a map of Kafka consumer configuration properties.
     * This method configures essential consumer properties such as bootstrap servers,
     * deserialization classes, and trusted packages for message deserialization.
     *
     * @return Map<String, Object> containing Kafka consumer properties.
     */
    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();

        // Set Kafka bootstrap server address
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Define the deserializer for message keys (String deserializer)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Define the deserializer for message values (Json deserializer)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Trust all packages for deserialization purposes
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return props;
    }

    /**
     * Creates a Kafka consumer factory which uses the provided configuration properties.
     * This factory will be used to create consumer instances for consuming Kafka messages.
     *
     * @return ConsumerFactory<String, Object> Kafka consumer factory.
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * Creates a Kafka listener container factory for concurrent message listeners.
     * This factory will set up the listener container used by Kafka listeners in the application.
     *
     * @return KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> Kafka listener container factory.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Set the consumer factory for the listener container
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}