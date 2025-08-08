package com.teamcubation.footmatchapi.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationTopics {

    @Bean
    public NewTopic notificationTopic() {

        return new NewTopic("notifications", 2, (short) 1);
    }

    @Bean
    public NewTopic notificationDltTopic() {
        return TopicBuilder.name("notifications.DLT")
                .partitions(2)
                .replicas(1)
                .config("retention.ms", "86400000")
                .build();
    }
}
