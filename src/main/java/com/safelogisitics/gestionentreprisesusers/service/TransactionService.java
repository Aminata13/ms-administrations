package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Date;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.payload.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RechargementTransactionRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

  public Page<Transaction> findByDateCreation(Date dateCreation, Pageable pageable);
  
  public Page<Transaction> findByAbonnement(String infosPersoId, Pageable pageable);

  public Page<Transaction> findByAbonnementAndDateCreation(String infosPersoId, Date dateCreation, Pageable pageable);

  public Page<Transaction> findByAbonnementAndAction(String infosPersoId, ETransactionAction action, Pageable pageable);

  public Page<Transaction> findByAbonnementAndActionAndDateCreation(String infosPersoId, ETransactionAction action, Date dateCreation, Pageable pageable);

  public Page<Transaction> findByCompteCreateur(String infosPersoId, Pageable pageable);

  public Page<Transaction> findByCompteCreateurAndDateCreation(String infosPersoId, Date dateCreation, Pageable pageable);

  public Page<Transaction> findByCompteCreateurAndActionAndDateCreation(String infosPersoId, ETransactionAction action, Date dateCreation, Pageable pageable);

  public Optional<Transaction> findByReference(String reference);
  
  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest);

  public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest);
}
