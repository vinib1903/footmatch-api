package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstadioTopics {

    @Bean
    public NewTopic estadiosCriacaoTopic() {
        return new NewTopic("estadios-criacao", 2, (short) 1);
    }

    @Bean
    public NewTopic estadiosAtualizacaoTopic() {
        return new NewTopic("estadios-atualizacao", 2, (short) 1);
    }
}
