package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PartidaTopics {

    @Bean
    public NewTopic partidasCriacaoTopic() {
        return new NewTopic("partidas-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasAtualizacaoTopic() {
        return new NewTopic("partidas-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasExclusaoTopic() {
        return new NewTopic("partidas-exclusao", 2, (short) 1);
    }
}
