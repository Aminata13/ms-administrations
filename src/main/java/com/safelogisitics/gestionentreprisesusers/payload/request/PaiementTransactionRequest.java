package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;

public class PaiementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @NotBlank
  private String password;

  @DecimalMin(value = "0")
  private BigDecimal montant;

  @NotBlank
  private String typePaiementId;

  private EServiceType service;

  @NotBlank
  private String serviceId;

  private String clientId;

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public String getPassword() {
    return this.password;
  }

  public BigDecimal getMontant() {
    return this.montant;
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
}
