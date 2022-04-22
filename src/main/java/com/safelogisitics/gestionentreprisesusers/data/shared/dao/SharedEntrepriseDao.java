package com.safelogisitics.gestionentreprisesusers.data.shared.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedEntrepriseModel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedEntrepriseDao extends PagingAndSortingRepository<SharedEntrepriseModel, String> {
  Optional<SharedEntrepriseModel> findByDenominationAndDeletedIsFalse(String denomination);

  Optional<SharedEntrepriseModel> findByDenominationOrNineaAndDeletedIsFalse(String denomination, String ninea);

  boolean existsByIdAndDeletedIsFalse(String id);

  boolean existsByDenominationAndDeletedIsFalse(String denomination);

  boolean existsByDenominationOrNineaAndDeletedIsFalse(String denomination, String ninea);
}
