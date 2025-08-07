package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
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

        return new NewTopic("clubes-criacao.DLT", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesAtualizacaoTopic() {

        return new NewTopic("clubes-atualizacao", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesAtualizacaoDltTopic() {

        return new NewTopic("clubes-atualizacao.DLT", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesExclusaoTopic() {

        return new NewTopic("clubes-exclusao", 2, (short) 1);
    }

    @Bean
    public NewTopic clubesExclusaoDltTopic() {

        return new NewTopic("clubes-exclusao.DLT", 2, (short) 1);
    }
}
