package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Abonnement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementDao extends PagingAndSortingRepository<Abonnement, String> {
  Page<Abonnement> findByDeletedIsFalseOrderByDateCreationDesc(Pageable pageable);

  Page<Abonnement> findByTypeAbonnementIdAndDeletedIsFalse(String typeAbonnementId, Pageable pageable);

  Page<Abonnement> findByStatutAndDeletedIsFalse(int statut, Pageable pageable);

  Page<Abonnement> findByCompteCreateurIdAndDeletedIsFalseOrderByDateCreationDesc(String CompteCreateurId, Pageable pageable);

  Optional<Abonnement> findByCompteClientIdAndDeletedIsFalse(String compteClientId);

  Optional<Abonnement> findByCompteClientId(String compteClientId);

  Optional<Abonnement> findByEntrepriseId(String compteClientId);

  Optional<Abonnement> findByCompteClientInfosPersoIdAndDeletedIsFalse(String infosPersoId);

  Optional<Abonnement> findByNumeroCarte(String numeroCarte);

  boolean existsByCompteClientInfosPersoIdAndNumeroCarteAndDeletedIsFalse(String infosPersoId, String numeroCarte);

  boolean existsByCompteClientInfosPersoId(String infosPersoId);

  boolean existsByCompteClientEntrepriseId(String entrepriseId);

  boolean existsByCompteClientInfosPersoIdOrNumeroCarte(String infosPersoId, String numeroCarte);

  boolean existsByNumeroCarte(String numeroCarte);
}
