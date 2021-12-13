package com.safelogisitics.gestionentreprisesusers.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  @Value(value = "${kafka.topics.createPaiement.name}")
  private String createPaiementTopicName;

  @Value(value = "${kafka.topics.blacklistAccesstoken.name}")
  private String blacklistAccesstokenName;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic topic1() {
    return new NewTopic(createPaiementTopicName, 1, (short) 1);
  }

  @Bean
  public NewTopic topic2() {
    return new NewTopic(blacklistAccesstokenName, 1, (short) 1);
  }
}
