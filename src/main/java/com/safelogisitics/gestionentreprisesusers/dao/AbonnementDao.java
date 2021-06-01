package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementDao extends PagingAndSortingRepository<Abonnement, String> {
  Page<Abonnement> findByDeletedIsFalse(Pageable pageable);

  Page<Abonnement> findByTypeAbonnementAndDeletedIsFalse(TypeAbonnement typeAbonnement, Pageable pageable);

  Page<Abonnement> findByStatutAndDeletedIsFalse(int statut, Pageable pageable);

  Page<Abonnement> findByCompteCreateurAndDeletedIsFalse(Compte CompteCreateur, Pageable pageable);

  Optional<Abonnement> findByCompteClientAndDeletedIsFalse(Compte typeAbonnement);

  Optional<Abonnement> findByCompteClient(Compte typeAbonnement);

  boolean existsByCompteClient(Compte compteClient);

  boolean existsByNumeroCarte(String numeroCarte);

}
