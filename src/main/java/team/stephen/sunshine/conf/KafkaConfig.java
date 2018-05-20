package team.stephen.sunshine.conf;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Stephen
 */
@Configuration
public class KafkaConfig {
    @Bean
    public Producer producer(@Value("${kafka.bootstrap.servers}") String bootstrapServers,
                             @Value("${kafka.key.serializer}") String keySerializer,
                             @Value("${kafka.value.serializer}") String valueSerializer) {
        Properties kafaProducerProps = new Properties();
        kafaProducerProps.put("bootstrap.servers", bootstrapServers);
        kafaProducerProps.put("key.serializer", keySerializer);
        kafaProducerProps.put("value.serializer", valueSerializer);
        return new KafkaProducer(kafaProducerProps);
    }

    @Bean
    public Consumer consumer(@Value("${kafka.bootstrap.servers}") String bootstrapServers,
                             @Value("${kafka.group.id}") String groupId,
                             @Value("${kafka.key.deserializer}") String keyDeserializer,
                             @Value("${kafka.value.deserializer}") String valueDeserializer) {
        Properties kafaConsumerProps = new Properties();
        kafaConsumerProps.put("bootstrap.servers", bootstrapServers);
        kafaConsumerProps.put("group.id", groupId);
        kafaConsumerProps.put("key.deserializer", keyDeserializer);
        kafaConsumerProps.put("value.deserializer", valueDeserializer);
        return new KafkaConsumer(kafaConsumerProps);
    }
}
