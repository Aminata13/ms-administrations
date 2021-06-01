package com.safelogisitics.gestionentreprisesusers.dao;

import java.time.LocalDate;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends PagingAndSortingRepository<Transaction, String> {
  Page<Transaction> findByDateCreationOrderByDateCreationDesc(LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByAbonnementOrderByDateCreationDesc(Abonnement abonnement, Pageable pageable);

  Page<Transaction> findByAbonnementAndDateCreationOrderByDateCreationDesc(Abonnement abonnement, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByAbonnementAndActionOrderByDateCreationDesc(Abonnement abonnement, ETransactionAction action, Pageable pageable);

  Page<Transaction> findByAbonnementAndActionAndDateCreationOrderByDateCreationDesc(Abonnement abonnement, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurOrderByDateCreationDesc(Compte compteCreateur, Pageable pageable);

  Page<Transaction> findByCompteCreateurAndDateCreationOrderByDateCreationDesc(Compte compteCreateur, LocalDate dateCreation, Pageable pageable);

  Page<Transaction> findByCompteCreateurAndActionAndDateCreationOrderByDateCreationDesc(Compte compteCreateur, ETransactionAction action, LocalDate dateCreation, Pageable pageable);

  Optional<Transaction> findByReference(String reference);

  boolean existsByReference(String reference);
}
