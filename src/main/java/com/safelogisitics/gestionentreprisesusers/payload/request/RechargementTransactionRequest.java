package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

public class RechargementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @DecimalMin(value = "0.0", inclusive = false)
  @Digits(integer=3, fraction=2)
  private BigDecimal montant;

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }
}
