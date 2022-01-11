package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

import java.math.BigDecimal;

import com.safelogisitics.gestionentreprisesusers.data.model.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETransactionType;

import org.springframework.stereotype.Component;

@Component
public class CreatePaiementDto {

  private String typePaiementId;

  private String reference;

  private ETransactionType compteDebiter;

  private EServiceType service;

  private String serviceId;

  private String clientId;

  private String auteurId;

  private BigDecimal montant;

  public CreatePaiementDto() {}

  public CreatePaiementDto(String typePaiementId, String reference, ETransactionType compteDebiter, EServiceType service, String serviceId, String clientId, String auteurId, BigDecimal montant) {
    this.typePaiementId = typePaiementId;
    this.reference = reference;
    this.compteDebiter = compteDebiter;
    this.service = service;
    this.serviceId = serviceId;
    this.clientId = clientId;
    this.auteurId = auteurId;
    this.montant = montant;
  }

  public String getTypePaiementId() {
    return this.typePaiementId;
  }

  public void setTypePaiementId(String typePaiementId) {
    this.typePaiementId = typePaiementId;
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public ETransactionType getCompteDebiter() {
    return this.compteDebiter;
  }

  public void setCompteDebiter(ETransactionType compteDebiter) {
    this.compteDebiter = compteDebiter;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getServiceId() {
    return this.serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getAuteurId() {
    return this.auteurId;
  }

  public void setAuteurId(String auteurId) {
    this.auteurId = auteurId;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }
}
