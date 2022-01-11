package com.safelogisitics.gestionentreprisesusers.web.kafkaListener;

import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnnulerPaiementCarteListener {

  @Autowired
  private TransactionService transactionService;

  @KafkaListener(topics = "${kafka.topics.annulerPaiementCarteService.name}", containerFactory = "annulerPaiementKafkaListenerContainerFactory", autoStartup = "${kafka.enabled}")
  public void listenAnnulationPaiement(PaiementServiceDto paiementDto) {
    transactionService.annulerPaiementTransaction(paiementDto);
  }
}
