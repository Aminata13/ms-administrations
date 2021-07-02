package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class RechargementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @DecimalMin(value = "1000", message = "Montant invalide")
  private BigDecimal montant;

  private String agentPassword;

  public RechargementTransactionRequest(String numeroCarte, BigDecimal montant) {
    this.numeroCarte = numeroCarte;
    this.montant = montant;
  }

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public String getAgentPassword() {
    return this.agentPassword;
  }
}
