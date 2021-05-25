package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteDao extends PagingAndSortingRepository<Compte, String> {
  Collection<Compte> findByInfosPersoId(String infosPersoId);

  Collection<Compte> findByType(ECompteType type);

  Optional<Compte> findByInfosPersoIdAndType(String infosPerso, ECompteType type);
  
  Optional<Compte> findByInfosPersoIdAndEntreprise(String infosPersoId, Entreprise entreprise);

  boolean existsByInfosPersoId(String infosPersoId);
  
  boolean existsByInfosPersoIdAndEntreprise(String infosPersoId, Entreprise entreprise);

  boolean existsByInfosPersoIdAndType(String infosPersoId, ECompteType type);

}
