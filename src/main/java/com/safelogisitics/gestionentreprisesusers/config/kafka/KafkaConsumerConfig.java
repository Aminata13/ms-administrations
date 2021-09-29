package com.safelogisitics.gestionentreprisesusers.config.kafka;

import java.util.HashMap;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.dto.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.dto.SmsCreateCommandeDto;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  public ConsumerFactory<String, PaiementServiceDto> annulerPaiementConsumerFactory() {
    JsonDeserializer<PaiementServiceDto> deserializer = new JsonDeserializer<>(PaiementServiceDto.class);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "paiementServiceDto");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PaiementServiceDto> annulerPaiementKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PaiementServiceDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(annulerPaiementConsumerFactory());
    return factory;
  }

  public ConsumerFactory<String, SmsCreateCommandeDto> smsCreateCommandeConsumerFactory() {
    JsonDeserializer<SmsCreateCommandeDto> deserializer = new JsonDeserializer<>(SmsCreateCommandeDto.class);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "smsCreateCommandeDto");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, SmsCreateCommandeDto> smsCreateCommandeKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, SmsCreateCommandeDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(smsCreateCommandeConsumerFactory());
    return factory;
  }

}