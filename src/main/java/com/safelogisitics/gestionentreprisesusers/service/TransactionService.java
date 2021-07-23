package com.safelogisitics.gestionentreprisesusers.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.payload.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RechargementTransactionRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

  public Page<Transaction> findByDateCreation(LocalDate dateCreation, Pageable pageable);
  
  public Page<TransactionDefaultFields> findByAbonnement(String infosPersoId, Pageable pageable);

  public Page<Transaction> findByAbonnementAndDateCreation(String infosPersoId, LocalDate dateCreation, Pageable pageable);

  public Page<Transaction> findByAbonnementAndAction(String infosPersoId, ETransactionAction action, Pageable pageable);

  public Page<Transaction> findByAbonnementAndActionAndDateCreation(String infosPersoId, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  public Page<Transaction> findByCompteCreateur(String infosPersoId, Pageable pageable);

  public Page<Transaction> findByCompteCreateurAndDateCreation(String infosPersoId, LocalDate dateCreation, Pageable pageable);

  public Page<Transaction> findByCompteCreateurAndActionAndDateCreation(String infosPersoId, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  public Page<Map<String, Object>> findByCompteCreateurAndAction(ETransactionAction action, Pageable pageable);

  public Page<Map<String, Object>> findTransactionsEnApprobations(Pageable pageable);

  public Map<String, Set<String>> approuveTransaction(ApprouveTransactionRequest request);

  public Optional<Transaction> findByReference(String reference);
  
  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest, ECompteType compteCreateur);

  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest);

  public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest);

  public ByteArrayInputStream getRapportByAbonnement(String id, String rapportType, String dateDebut, String dateFin);
}
