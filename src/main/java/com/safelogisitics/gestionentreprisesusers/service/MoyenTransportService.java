package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.MoyenTransportSearchDto;
import com.safelogisitics.gestionentreprisesusers.data.model.MoyenTransport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MoyenTransportService {

  public Page<MoyenTransport> getMoyenTransports(MoyenTransportSearchDto moyenTransportSearch, Pageable pageable);

  public Optional<MoyenTransport> getMoyenTransportById(String id);

  public Collection<MoyenTransport> searchMoyenTransport(String searchValue);

  public MoyenTransport createMoyenTransport(MoyenTransport moyenTransport);

  public MoyenTransport updateMoyenTransport(String id, MoyenTransport moyenTransport);

  public void deleteMoyenTransport(String id);
}
