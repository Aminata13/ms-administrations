package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.PaiementValidation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementValidationDao extends PagingAndSortingRepository<PaiementValidation, String> {
  Optional<PaiementValidation> findByCodeValidationAndNumeroCommande(String codeValidation, String numeroCommande);

  Optional<PaiementValidation> findByNumeroCarteAndNumeroCommande(String numeroCarte, String numeroCommande);

  Optional<PaiementValidation> findByNumeroCommande(String numeroCommande);

  boolean existsByNumeroCarteAndCodeValidation(String numeroCarte, String codeValidation);
}
