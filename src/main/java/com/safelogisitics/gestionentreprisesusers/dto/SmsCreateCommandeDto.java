package com.safelogisitics.gestionentreprisesusers.dto;

import java.math.BigDecimal;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;

public class SmsCreateCommandeDto {
  
  private EServiceType service;

  private String serviceId;

  private String clientId;

  private String numeroCommande;

  private String codeRetrait;

  private BigDecimal montant;

  public SmsCreateCommandeDto() { }

  public SmsCreateCommandeDto(String serviceId, String clientId, String numeroCommande, String codeRetrait, BigDecimal montant) {
    this.service = EServiceType.LIVRAISON;
    this.serviceId = serviceId;
    this.clientId = clientId;
    this.numeroCommande = numeroCommande;
    this.codeRetrait = codeRetrait;
    this.montant = montant;
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

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public String getCodeRetrait() {
    return this.codeRetrait;
  }

  public void setCodeRetrait(String codeRetrait) {
    this.codeRetrait = codeRetrait;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

}
