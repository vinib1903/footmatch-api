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
    public NewTopic partidasCriacaoDltTopic() {

        return new NewTopic("partidas-criacao.DLT", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasAtualizacaoTopic() {

        return new NewTopic("partidas-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasAtualizacaoDltTopic() {

        return new NewTopic("partidas-atualizacao.DLT", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasExclusaoTopic() {

        return new NewTopic("partidas-exclusao", 2, (short) 1);
    }

    @Bean
    public NewTopic partidasExclusaoDltTopic() {

        return new NewTopic("partidas-exclusao.DLT", 2, (short) 1);
    }
}
