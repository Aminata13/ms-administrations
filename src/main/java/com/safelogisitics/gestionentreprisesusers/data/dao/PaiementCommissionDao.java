package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementCommissionDao extends PagingAndSortingRepository<PaiementCommissionModel, String> {

  public Optional<PaiementCommissionModel> findBycommmandeIds(String id);
}
