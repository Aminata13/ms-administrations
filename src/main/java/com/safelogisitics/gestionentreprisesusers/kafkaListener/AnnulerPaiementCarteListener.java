package com.safelogisitics.gestionentreprisesusers.kafkaListener;

import com.safelogisitics.gestionentreprisesusers.dto.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnnulerPaiementCarteListener {

  @Autowired
  private TransactionService transactionService;

  @KafkaListener(topics = "${kafka.topics.annulerPaiementCarteService.name}", containerFactory = "annulerPaiementKafkaListenerContainerFactory", autoStartup = "${kafka.enabled}")
  public void listenCreationPaiement(PaiementServiceDto paiementDto) {
    transactionService.annulerPaiementTransaction(paiementDto);
  }
}
