package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.TypeEvenement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeEvenementDao extends PagingAndSortingRepository<TypeEvenement, String> {
  Optional<TypeEvenement> findByLibelle(String libelle);

  boolean existsByLibelle(String libelle);
}