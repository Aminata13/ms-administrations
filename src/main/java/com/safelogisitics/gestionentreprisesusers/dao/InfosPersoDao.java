package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfosPersoDao extends PagingAndSortingRepository<InfosPerso, String> {
  Optional<InfosPerso> findByEmail(String email);

  Boolean existsByEmail(String email);

  Boolean existsByEmailOrTelephone(String email, String telephone);

  
}
