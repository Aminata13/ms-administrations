package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteDao extends PagingAndSortingRepository<Compte, String> {
  Collection<Compte> findByInfosPersoId(String infosPersoId);

  Collection<Compte> findByTypeAndDeletedIsFalse(ECompteType type);

  Optional<Compte> findByInfosPersoIdAndType(String infosPerso, ECompteType type);

  Optional<Compte> findByIdAndType(String id, ECompteType type);

  Optional<Compte> findByNumeroEmei(String numeroEmei);
  
  Optional<Compte> findByInfosPersoIdAndEntrepriseId(String infosPersoId, String entrepriseId);

  Optional<Compte> findByInfosPersoIdAndTypeAndDeletedIsFalse(String infosPersoId, ECompteType type);

  boolean existsByInfosPersoId(String infosPersoId);
  
  boolean existsByInfosPersoIdAndEntrepriseId(String infosPersoId, String entrepriseId);

  boolean existsByInfosPersoIdAndType(String infosPersoId, ECompteType type);

  boolean existsByInfosPersoIdAndTypeAndDeletedIsFalse(String infosPersoId, ECompteType type);

  boolean existsByInfosPersoIdAndDeletedIsFalse(String infosPersoId);


  long countByInfosPersoIdAndDeletedIsFalse(String infosPersoId);
}
