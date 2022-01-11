package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.TypeAbonnement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAbonnementDao extends PagingAndSortingRepository<TypeAbonnement, String> {
  Optional<TypeAbonnement> findByLibelle(String libelle);

  boolean existsByLibelle(String libelle);
}
