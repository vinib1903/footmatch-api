package com.teamcubation.footmatchapi.jobs;

import com.teamcubation.footmatchapi.service.kafka.NotificationServiceKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTopicMonitorTask {

    private final AdminClient adminClient;
    private final NotificationServiceKafka notificationServiceKafka;
    private final KafkaProperties kafkaProperties;

    @Value("${kafka.dlt.topics-to-monitor}")
    private List<String> dltTopics;

    @Value("${kafka.dlt.message-threshold}")
    private int messageThreshold;

    @Scheduled(cron = "${kafka.dlt.monitor-cron}")
    public void monitorDltTopics() {
        //log.info("Iniciando monitoramento dos tópicos DLT: {}", dltTopics);
        log.info("Iniciando monitoramento dos tópicos DLT");

        try {
            Set<String> existingTopics = adminClient.listTopics().names().get();
            for (String topicName : dltTopics) {
                if (!existingTopics.contains(topicName)) {
                    log.debug("Tópico DLT '{}' não encontrado. Pulando a verificação.", topicName);
                    continue;
                }
                try {
                    long messageCount = getTopicMessageCount(topicName);
                    if (messageCount > 0) {
                        log.info("O tópico {} contém {} mensagens", topicName, messageCount);
                    }

                    if (messageCount >= messageThreshold) {
                        String report = createDltReport(topicName);
                        String notificationMessage = String.format(
                                "Alerta: O tópico DLT '%s' atingiu o limite de %d mensagens, contendo %d mensagens.\n\n%s",
                                topicName, messageThreshold, messageCount, report
                        );
                        notificationServiceKafka.sendNotification(notificationMessage);
                    }
                } catch (Exception e) {
                    log.error("Erro ao processar o tópico DLT: {}", topicName, e);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao listar os tópicos do Kafka.", e);
        }
        log.info("Monitoramento dos tópicos DLT concluído.");
    }

    private String createDltReport(String topicName) {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties(null);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "dlt-monitor-group-" + UUID.randomUUID().toString());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        StringBuilder reportBuilder = new StringBuilder("--- Relatório de Mensagens na DLT ---\n\n");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            List<TopicPartition> partitions = consumer.partitionsFor(topicName).stream()
                    .map(p -> new TopicPartition(topicName, p.partition()))
                    .collect(Collectors.toList());
            consumer.assign(partitions);

            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
            List<ConsumerRecord<String, String>> allRecords = new ArrayList<>();
            boolean allPartitionsFinished = false;

            while (!allPartitionsFinished) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(allRecords::add);

                allPartitionsFinished = true;
                for (TopicPartition partition : partitions) {
                    if (consumer.position(partition) < endOffsets.get(partition)) {
                        allPartitionsFinished = false;
                        break;
                    }
                }
            }

            if (allRecords.isEmpty()) {
                return "Nenhuma mensagem encontrada na DLT para gerar o relatório.";
            }

            allRecords.sort(Comparator.comparing(ConsumerRecord::offset));

            for (ConsumerRecord<String, String> record : allRecords) {
                String originalTopic = getHeaderValue(record, KafkaHeaders.DLT_ORIGINAL_TOPIC);
                String exceptionMessage = getHeaderValue(record, KafkaHeaders.DLT_EXCEPTION_MESSAGE);
                String cleanedReason = exceptionMessage;

                int lastSemicolonIndex = exceptionMessage.lastIndexOf(';');
                if (lastSemicolonIndex != -1) {
                    cleanedReason = exceptionMessage.substring(lastSemicolonIndex + 1).trim();
                }

                reportBuilder.append(String.format("--- Mensagem (Partição: %d, Offset: %d) ---\n", record.partition(), record.offset()));
                reportBuilder.append(String.format("Tópico de Origem: %s\n", originalTopic));
                reportBuilder.append(String.format("Payload: %s\n", record.value()));
                reportBuilder.append(String.format("Motivo da Falha: %s\n\n", cleanedReason));
            }
        } catch (Exception e) {
            log.error("Erro ao criar relatório para o tópico DLT {}: {}", topicName, e.getMessage());
            return "Falha ao gerar o relatório detalhado. Verifique os logs do sistema.";
        }

        return reportBuilder.toString();
    }

    private String getHeaderValue(ConsumerRecord<String, String> record, String headerName) {
        return Optional.ofNullable(record.headers().lastHeader(headerName))
                .map(header -> new String(header.value(), StandardCharsets.UTF_8))
                .orElse("N/A");
    }

    private long getTopicMessageCount(String topicName) throws ExecutionException, InterruptedException {
        List<TopicPartition> partitions = adminClient.describeTopics(Collections.singletonList(topicName))
                .allTopicNames().get().get(topicName).partitions()
                .stream()
                .map(p -> new TopicPartition(topicName, p.partition()))
                .collect(Collectors.toList());

        Map<TopicPartition, OffsetSpec> requestOffsets = partitions.stream()
                .collect(Collectors.toMap(p -> p, p -> OffsetSpec.latest()));

        ListOffsetsResult offsetsResult = adminClient.listOffsets(requestOffsets);

        long totalMessages = 0;
        for (TopicPartition partition : partitions) {
            totalMessages += offsetsResult.partitionResult(partition).get().offset();
        }
        return totalMessages;
    }
}
