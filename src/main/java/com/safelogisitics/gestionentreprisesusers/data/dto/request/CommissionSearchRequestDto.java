package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

public class CommissionSearchRequestDto {
  
  private String id;

  private Set<String> ids;

  private String numero;

  private EServiceType service;

  private String commandeId;

  private BigDecimal montant;

  private String responsableId;

  private Boolean payer;

  private String paiementId;

  @Min(value = 1000, message = "L'année est compris entre 1000 et 9999.")
  @Max(value = 9999, message = "L'année est compris entre 1000 et 9999.")
  private Integer annee;

  @Min(value = 1, message = "Le mois est compris entre 1 et 12.")
  @Max(value = 12, message = "Le mois est compris entre 1 et 12.")
  private Integer mois;

  private String dateDebut;

  private String dateFin;

  public CommissionSearchRequestDto() {}

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<String> getIds() {
    return this.ids;
  }

  public void setIds(Set<String> ids) {
    this.ids = ids;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getCommandeId() {
    return this.commandeId;
  }

  public void setCommandeId(String commandeId) {
    this.commandeId = commandeId;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

  public Boolean isPayer() {
    return this.payer;
  }

  public void setPayer(Boolean payer) {
    this.payer = payer;
  }

  public String getPaiementId() {
    return this.paiementId;
  }

  public void setPaiementId(String paiementId) {
    this.paiementId = paiementId;
  }

  public Integer getAnnee() {
    return this.annee;
  }

  public void setAnnee(Integer annee) {
    this.annee = annee;
  }

  public Integer getMois() {
    return this.mois;
  }

  public void setMois(Integer mois) {
    this.mois = mois;
  }

  public String getDateDebut() {
    return this.dateDebut;
  }

  public void setDateDebut(String dateDebut) {
    this.dateDebut = dateDebut;
  }

  public String getDateFin() {
    return this.dateFin;
  }

  public void setDateFin(String dateFin) {
    this.dateFin = dateFin;
  }
}
