package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.Service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDao extends PagingAndSortingRepository<Service, String> {
  Optional<Service> findByLibelle(String libelle);

  Collection<Service> findByPublicService(boolean publicService);

  boolean existsByLibelle(String libelle);
}
