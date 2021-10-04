package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Entreprise;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepriseDao extends PagingAndSortingRepository<Entreprise, String> {
  Optional<Entreprise> findByDenominationAndDeletedIsFalse(String denomination);

  Optional<Entreprise> findByDenominationOrNineaAndDeletedIsFalse(String denomination, String ninea);

  boolean existsByIdAndDeletedIsFalse(String id);

  boolean existsByDenominationAndDeletedIsFalse(String denomination);

  boolean existsByDenominationOrNineaAndDeletedIsFalse(String denomination, String ninea);
}
