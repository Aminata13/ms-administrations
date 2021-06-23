package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipementDao extends PagingAndSortingRepository<Equipement, String> {
  Optional<Equipement> findByLibelle(String libelle);

  Collection<Equipement> findBytype(EEquipementType type);

  boolean existsByLibelle(String libelle);
}
