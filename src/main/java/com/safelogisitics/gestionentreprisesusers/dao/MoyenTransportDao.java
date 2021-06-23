package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.model.enums.EMoyenTransportType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoyenTransportDao extends PagingAndSortingRepository<MoyenTransport, String> {
  Optional<MoyenTransport> findByReference(String reference);

  Collection<MoyenTransport> findBytype(EMoyenTransportType type);

  boolean existsByReference(String reference);
}
