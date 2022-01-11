package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

public class ApprouveTransactionRequest {
  
  @NotNull
  private int approbation;

  @NotNull
  private Set<String> transactionIds;

  public int getApprobation() {
    return this.approbation;
  }

  public Set<String> getTransactionIds() {
    return this.transactionIds;
  }
}
