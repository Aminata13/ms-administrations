package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.EMoyenTransportType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoyenTransportDao extends PagingAndSortingRepository<MoyenTransport, String> {

  Optional<MoyenTransport> findByReference(String reference);

  Collection<MoyenTransport> findBytype(EMoyenTransportType type);

  boolean existsByReference(String reference);

  boolean existsByNumeroSerie(String numeroSerie);

  boolean existsByNumeroCarteGrise(String numeroCarteGrise);

  boolean existsByMatricule(String matricule);

}
