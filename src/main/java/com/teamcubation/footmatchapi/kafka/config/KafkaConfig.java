package com.teamcubation.footmatchapi.kafka.config;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    @Bean
    public ProducerFactory<String, PartidaRequestDTO> producerFactory() {

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, PartidaRequestDTO> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, PartidaRequestDTO> consumerFactory() {

        JsonDeserializer<PartidaRequestDTO> deserializer = new JsonDeserializer<>(PartidaRequestDTO.class, false);
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "partida-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PartidaRequestDTO> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PartidaRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public NewTopic partidasCriacaoTopic() {

        return new NewTopic("partidas-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasAtualizacaoTopic() {

        return new NewTopic("partidas-atualizacao", 2, (short) 1);
    }
}