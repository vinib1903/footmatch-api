package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstadioTopics {

    @Bean
    public NewTopic estadiosCriacaoTopic() {
        return new NewTopic("estadios-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic estadiosCriacaoDltTopic() {
        return TopicBuilder.name("estadios-criacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }

    @Bean
    public NewTopic estadiosAtualizacaoTopic() {
        return new NewTopic("estadios-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic estadiosAtualizacaoDltTopic() {
        return TopicBuilder.name("estadios-atualizacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }
}
