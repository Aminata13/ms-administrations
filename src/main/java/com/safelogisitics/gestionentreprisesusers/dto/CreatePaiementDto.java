package com.safelogisitics.gestionentreprisesusers.dto;

import java.math.BigDecimal;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;

public class CreatePaiementDto {

  private String typePaiementId;

  private String reference;

  private EServiceType service;

  private String serviceId;

  private String clientId;

  private BigDecimal montant;

  public CreatePaiementDto() {}

  public CreatePaiementDto(String typePaiementId, String reference, EServiceType service, String serviceId, String clientId, BigDecimal montant) {
    this.typePaiementId = typePaiementId;
    this.reference = reference;
    this.service = service;
    this.serviceId = serviceId;
    this.clientId = clientId;
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

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }
}
