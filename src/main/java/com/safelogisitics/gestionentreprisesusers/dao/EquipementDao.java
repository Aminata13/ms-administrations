package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementCategorie;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipementDao extends PagingAndSortingRepository<Equipement, String> {
  Optional<Equipement> findByLibelle(String libelle);

  Collection<Equipement> findByCategories(EEquipementCategorie type);

  Optional<Equipement> findByLibelleAndCategories(String libelle, Set<EEquipementCategorie> categories);

  boolean existsByLibelle(String libelle);

  boolean existsByLibelleAndCategories(String libelle, Set<EEquipementCategorie> categories);

  boolean existsByHistoriqueFournituresNumeroCommande(String numeroCommande);
}
