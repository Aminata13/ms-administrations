package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.List;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfosPersoDao extends PagingAndSortingRepository<InfosPersoModel, String> {
  Page<InfosPersoModel> findByComptesIsNull(Pageable pageable);

  Page<InfosPersoModel> findByIdInOrderByDateCreationDesc(List<String> ids, Pageable pageable);

  Optional<InfosPersoModel> findByEmail(String email);

  Optional<InfosPersoModel> findByTelephone(String telephone);

  Optional<InfosPersoModel> findByEmailOrTelephone(String email, String telephone);

  boolean existsByEmail(String email);

  boolean existsByTelephone(String telephone);

  boolean existsByEmailOrTelephone(String email, String telephone);  
}
