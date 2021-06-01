package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;

public class PaiementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @NotBlank
  private String password;

  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer=3, fraction=2)
  private BigDecimal montant;

  @NotBlank
  private EServiceType service;

  @NotBlank
  private String serviceId;

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public String getPassword() {
    return this.password;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public EServiceType getService() {
    return this.service;
  }

  public String getServiceId() {
    return this.serviceId;
  }
}
