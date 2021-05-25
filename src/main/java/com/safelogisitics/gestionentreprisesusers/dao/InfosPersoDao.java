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
  // @Query("{ 'comptes': { $exists:true, $size:0 }}")
  
  // @Query(value = "{'comptes': ?0}", fields = "{'comptes' : 0}")
  Page<InfosPerso> findByComptesIsNull(Pageable pageable);

  Page<InfosPerso> findByIdIn(List<String> ids, Pageable pageable);

  Optional<InfosPerso> findByEmail(String email);

  Optional<InfosPerso> findByTelephone(String telephone);

  Optional<InfosPerso> findByEmailOrTelephone(String email, String telephone);

  boolean existsByEmail(String email);

  boolean existsByEmailOrTelephone(String email, String telephone);  
}
