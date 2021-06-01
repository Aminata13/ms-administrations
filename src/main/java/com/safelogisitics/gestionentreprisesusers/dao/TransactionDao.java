package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends PagingAndSortingRepository<Transaction, String> {
  Collection<Transaction> findByAbonnementOrderByDateCreationDesc(Abonnement abonnement);

  Collection<Transaction> findByAbonnementAndDateCreationOrderByDateCreationDesc(Abonnement abonnement, Date dateCreation);

  Collection<Transaction> findByAbonnementAndActionOrderByDateCreationDesc(Abonnement abonnement, ETransactionAction action);

  Collection<Transaction> findByAbonnementAndActionAndDateCreationOrderByDateCreationDesc(Abonnement abonnement, ETransactionAction action, Date dateCreation);

  Collection<Transaction> findByCompteCreateurOrderByDateCreationDesc(Compte compteCreateur);

  Collection<Transaction> findByCompteCreateurAndDateCreationOrderByDateCreationDesc(Compte compteCreateur, Date dateCreation);

  Collection<Transaction> findByCompteCreateurAndActionAndDateCreationOrderByDateCreationDesc(Compte compteCreateur, ETransactionAction action, Date dateCreation);

  Optional<Transaction> findByReference(String reference);

  boolean existsByReference(String reference);
}
