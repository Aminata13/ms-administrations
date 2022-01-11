package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.NumeroCarte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumeroCarteDao extends PagingAndSortingRepository<NumeroCarte, String> {
  Optional<NumeroCarte> findByNumero(String numero);

  Page<NumeroCarte> findByTypeAbonnementId(String typeAbonnementId, Pageable pageable);

  boolean existsByNumero(String numero);

}
