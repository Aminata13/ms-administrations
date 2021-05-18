package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteDao extends PagingAndSortingRepository<Compte, String> {
  Optional<Compte> findByInfosPerso(InfosPerso infosPerso);

  Optional<Compte> findByInfosPersoAndType(InfosPerso infosPerso, ECompteType type);
  
  Optional<Compte> findByInfosPersoAndEntreprise(InfosPerso infosPerso, Entreprise entreprise);

  Boolean existsByInfosPerso(InfosPerso infosPerso);
  
  Boolean existsByInfosPersoAndEntreprise(InfosPerso infosPerso, Entreprise entreprise);

  Boolean existsByInfosPersoAndType(InfosPerso infosPerso, ECompteType type);

}
