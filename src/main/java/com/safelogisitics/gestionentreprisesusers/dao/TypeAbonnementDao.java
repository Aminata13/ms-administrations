package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAbonnementDao extends PagingAndSortingRepository<TypeAbonnement, String> {
  Optional<TypeAbonnement> findByLibelle(String libelle);

  boolean existsByLibelle(String libelle);
}
