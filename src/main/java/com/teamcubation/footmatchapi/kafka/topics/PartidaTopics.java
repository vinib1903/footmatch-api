package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PartidaTopics {

    @Bean
    public NewTopic partidasCriacaoTopic() {

        return new NewTopic("partidas-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasCriacaoDltTopic() {
        return TopicBuilder.name("partidas-criacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }

    @Bean
    public NewTopic partidasAtualizacaoTopic() {

        return new NewTopic("partidas-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasAtualizacaoDltTopic() {
        return TopicBuilder.name("partidas-atualizacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }

    @Bean
    public NewTopic partidasExclusaoTopic() {

        return new NewTopic("partidas-exclusao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasExclusaoDltTopic() {
        return TopicBuilder.name("partidas-exclusao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }
}
