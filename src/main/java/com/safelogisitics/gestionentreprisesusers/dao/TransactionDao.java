package com.safelogisitics.gestionentreprisesusers.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends PagingAndSortingRepository<Transaction, String> {
  Page<Transaction> findByDateCreationOrderByDateCreationDesc(LocalDateTime dateCreation, Pageable pageable);

  Page<TransactionDefaultFields> findByAbonnementIdOrderByDateCreationDesc(String abonnementId, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndDateCreationGreaterThanEqual(String abonnementId, LocalDateTime dateCreation, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndActionOrderByDateCreationDesc(String abonnementId, ETransactionAction action, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndActionAndDateCreationGreaterThanEqual(String abonnementId, ETransactionAction action, LocalDateTime dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdOrderByDateCreationDesc(String compteCreateurId, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdAndDateCreationGreaterThanEqual(String compteCreateurId, LocalDateTime dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdAndActionAndDateCreationGreaterThanEqual(String compteCreateur, ETransactionAction action, LocalDateTime dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdAndActionOrderByDateCreationDesc(String compteCreateur, ETransactionAction action, Pageable pageable);

  Page<Transaction> findByActionAndApprobation(ETransactionAction action, int approbation, Pageable pageable);

  Optional<Transaction> findByReference(String reference);

  Optional<Transaction> findByIdAndApprobation(String id, int approbation);

  boolean existsByReference(String reference);
}
