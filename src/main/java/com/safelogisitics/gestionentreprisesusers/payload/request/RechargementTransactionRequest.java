package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class RechargementTransactionRequest {
  
  @NotBlank
  private String numeroCarte;

  @DecimalMin(value = "1000", message = "Montant doit être supérieur ou égal à 1 000")
  private BigDecimal montant;

  @Min(value = 1, message = "le nombre de points doit être supérieur 0")
  private Long points;

  private String agentPassword;

  public RechargementTransactionRequest() {}

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

  public Long getPoints() {
    return this.points;
  }

  public String getAgentPassword() {
    return this.agentPassword;
  }
}
