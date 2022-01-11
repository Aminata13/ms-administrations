package com.safelogisitics.gestionentreprisesusers.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementValidation;
import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETransactionType;

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

  public Page<Map<String, Object>> findMyHistoriqueTransactionsApprobations(Pageable pageable);

  public Map<String, Set<String>> approuveTransaction(ApprouveTransactionRequest request);

  public Optional<Transaction> findByReference(String reference);

  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest, ETransactionType transactionType, ECompteType compteCreateur);

  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest);

  public Boolean createPaiementValidation(PaiementValidation paiementValidationRequest);

  public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest);

  public void annulerPaiementTransaction(PaiementServiceDto paiementServiceDto);

  public ByteArrayInputStream getRapportByAbonnement(String id, String rapportType, String dateDebut, String dateFin);
}
