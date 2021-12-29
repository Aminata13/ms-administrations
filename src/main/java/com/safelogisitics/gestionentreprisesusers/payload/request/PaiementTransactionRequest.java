package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionType;

public class PaiementTransactionRequest {
  
  @NotBlank(message = "Le numéro de carte est obligatoire.")
  private String numeroCarte;

  @NotBlank(message = "Le code de validation est obligatoire.")
  private String codeValidation;

  @DecimalMin(value = "1000", message = "Montant doit être supérieur ou egal à 1 00")
  private BigDecimal montant;

  @Min(value = 1, message = "le nombre de points doit être supérieur 0")
  private Long points;

  @NotNull(message = "Veuillez préciser le compte à utiliser (solde ou points gratuites)")
  private ETransactionType compteDebiter;

  @NotBlank(message = "Le type de paiement est obligatoire.")
  private String typePaiementId;

  private EServiceType service;

  @NotBlank(message = "Le service est obligatoire.")
  private String serviceId;

  private String numeroCommande;

  private String clientId;

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public String getCodeValidation() {
    return this.codeValidation.replaceAll("\\D+","");
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public Long getPoints() {
    return this.points;
  }

  public ETransactionType getCompteDebiter() {
    return this.compteDebiter;
  }

  public String getTypePaiementId() {
    return this.typePaiementId;
  }

  public EServiceType getService() {
    return this.service;
  }

  public String getServiceId() {
    return this.serviceId;
  }

  public String getClientId() {
    return this.clientId;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }
}
