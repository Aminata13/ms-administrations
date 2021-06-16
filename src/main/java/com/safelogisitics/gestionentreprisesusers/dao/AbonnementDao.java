package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementDao extends PagingAndSortingRepository<Abonnement, String> {
  Page<Abonnement> findByDeletedIsFalse(Pageable pageable);

  Page<Abonnement> findByTypeAbonnementIdAndDeletedIsFalse(String typeAbonnementId, Pageable pageable);

  Page<Abonnement> findByStatutAndDeletedIsFalse(int statut, Pageable pageable);

  Page<Abonnement> findByCompteCreateurIdAndDeletedIsFalse(String CompteCreateurId, Pageable pageable);

  Optional<Abonnement> findByCompteClientIdAndDeletedIsFalse(String compteClientId);

  Optional<Abonnement> findByCompteClientId(String compteClientId);

  Optional<Abonnement> findByNumeroCarte(String numeroCarte);

  boolean existsByCompteClient(Compte compteClient);

  boolean existsByNumeroCarte(String numeroCarte);

}
