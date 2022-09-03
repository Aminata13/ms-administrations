package com.safelogisitics.gestionentreprisesusers.data.shared.dao;

import java.util.List;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedInfosPersoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedInfosPersoDao extends PagingAndSortingRepository<SharedInfosPersoModel, String> {
  Page<SharedInfosPersoModel> findByComptesIsNull(Pageable pageable);

  Page<SharedInfosPersoModel> findByIdInOrderByDateCreationDesc(List<String> ids, Pageable pageable);

  Optional<SharedInfosPersoModel> findByEmail(String email);

  Optional<SharedInfosPersoModel> findByTelephone(String telephone);

  Optional<SharedInfosPersoModel> findByEmailOrTelephone(String email, String telephone);

  Optional<SharedInfosPersoModel> findByComptesId(String id);

  boolean existsByEmail(String email);

  boolean existsByTelephone(String telephone);

  boolean existsByEmailOrTelephone(String email, String telephone);  
}
