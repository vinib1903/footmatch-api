package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClubeTopics {

    @Bean
    public NewTopic clubesCriacaoTopic() {

        return new NewTopic("clubes-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesCriacaoDltTopic() {
        return TopicBuilder.name("clubes-criacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }

    @Bean
    public NewTopic clubesAtualizacaoTopic() {

        return new NewTopic("clubes-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesAtualizacaoDltTopic() {
        return TopicBuilder.name("clubes-atualizacao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }

    @Bean
    public NewTopic clubesExclusaoTopic() {

        return new NewTopic("clubes-exclusao", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesExclusaoDltTopic() {
        return TopicBuilder.name("clubes-exclusao.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }
}
