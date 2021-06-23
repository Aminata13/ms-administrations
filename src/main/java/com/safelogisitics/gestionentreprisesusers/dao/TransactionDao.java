package com.safelogisitics.gestionentreprisesusers.dao;

import java.time.LocalDate;
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
  Page<Transaction> findByDateCreationOrderByDateCreationDesc(LocalDate dateCreation, Pageable pageable);

  Page<TransactionDefaultFields> findByAbonnementIdOrderByDateCreationDesc(String abonnementId, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndDateCreationOrderByDateCreationDesc(String abonnementId, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndActionOrderByDateCreationDesc(String abonnementId, ETransactionAction action, Pageable pageable);

  Page<Transaction> findByAbonnementIdAndActionAndDateCreationOrderByDateCreationDesc(String abonnementId, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdOrderByDateCreationDesc(String compteCreateurId, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdAndDateCreationOrderByDateCreationDesc(String compteCreateurId, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurIdAndActionAndDateCreationOrderByDateCreationDesc(String compteCreateur, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  Optional<Transaction> findByReference(String reference);

  boolean existsByReference(String reference);
}
