package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionDao extends PagingAndSortingRepository<CommissionModel, String> {
  
  public Optional<CommissionModel> findByNumeroAndService(String numero, EServiceType service);

  public boolean existsByNumeroAndService(String numero, EServiceType service);
}
