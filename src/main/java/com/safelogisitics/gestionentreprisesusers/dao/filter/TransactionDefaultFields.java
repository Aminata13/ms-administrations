package com.safelogisitics.gestionentreprisesusers.dao.filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;

import org.springframework.beans.factory.annotation.Value;

public interface TransactionDefaultFields {
  public String getId();

  @Value("#{target.getAbonnement().getId()}")
  public String getAbonnement();

  public String getReference();

  public ETransactionAction getAction();

  @Value("#{target.getCompteCreateur().getId()}")
  public String getCompteCreateur();

  public BigDecimal getMontant();

  public LocalDateTime getDateCreation();
}
