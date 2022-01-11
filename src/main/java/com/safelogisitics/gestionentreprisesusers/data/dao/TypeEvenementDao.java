package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.TypeEvenement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeEvenementDao extends PagingAndSortingRepository<TypeEvenement, String> {
  Optional<TypeEvenement> findByLibelle(String libelle);

  boolean existsByLibelle(String libelle);
}