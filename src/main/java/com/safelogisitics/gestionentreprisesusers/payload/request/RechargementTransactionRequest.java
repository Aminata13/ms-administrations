package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class RechargementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @DecimalMin(value = "0")
  private BigDecimal montant;

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public BigDecimal getMontant() {
    return this.montant;
  }
}
