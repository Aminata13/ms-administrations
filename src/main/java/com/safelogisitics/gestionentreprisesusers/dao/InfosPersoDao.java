package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.List;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfosPersoDao extends PagingAndSortingRepository<InfosPerso, String> {
  Page<InfosPerso> findByComptesIsNull(Pageable pageable);

  Page<InfosPerso> findByIdInOrderByDateCreationDesc(List<String> ids, Pageable pageable);

  Optional<InfosPerso> findByEmail(String email);

  Optional<InfosPerso> findByTelephone(String telephone);

  Optional<InfosPerso> findByEmailOrTelephone(String email, String telephone);

  boolean existsByEmail(String email);

  boolean existsByTelephone(String telephone);

  boolean existsByEmailOrTelephone(String email, String telephone);  
}
