package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;

public class ApprouveTransactionRequest {
  
  @NotBlank
  private int approbation;

  @NotBlank
  private Set<String> transactionIds;

  public int getApprobation() {
    return this.approbation;
  }

  public Set<String> getTransactionIds() {
    return this.transactionIds;
  }
}
