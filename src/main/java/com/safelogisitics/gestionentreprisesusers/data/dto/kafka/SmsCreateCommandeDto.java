package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

import java.math.BigDecimal;

import com.safelogisitics.gestionentreprisesusers.data.model.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.EServiceType;

public class SmsCreateCommandeDto {
  
  private EServiceType service;

  private EServiceConciergeType serviceConciergerie;

  private String serviceId;

  private String clientId;

  private String numeroCommande;

  private String codeRetrait;

  private BigDecimal montant;

  private String duree;

  public SmsCreateCommandeDto() { }

  public SmsCreateCommandeDto(EServiceConciergeType serviceConciergerie, String serviceId, String clientId, String numeroCommande, String codeRetrait, BigDecimal montant, String duree) {
    this.service = EServiceType.LIVRAISON;
    this.serviceConciergerie = serviceConciergerie;
    this.serviceId = serviceId;
    this.clientId = clientId;
    this.numeroCommande = numeroCommande;
    this.codeRetrait = codeRetrait;
    this.montant = montant;
    this.duree = duree;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public EServiceConciergeType getServiceConciergerie() {
    return serviceConciergerie;
  }

  public void setServiceConciergerie(EServiceConciergeType serviceConciergerie) {
    this.serviceConciergerie = serviceConciergerie;
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

  public String getDuree() {
    return this.duree;
  }

  public void setDuree(String duree) {
    this.duree = duree;
  }

}