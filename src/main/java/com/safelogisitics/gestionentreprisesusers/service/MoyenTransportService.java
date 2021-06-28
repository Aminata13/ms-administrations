package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.model.enums.EMoyenTransportType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MoyenTransportService {

  public Page<MoyenTransport> getMoyenTransports(String fournisseur, EMoyenTransportType type, String reference, String marque, String modele, Pageable pageable);

  public Optional<MoyenTransport> getMoyenTransportById(String id);
  
  public MoyenTransport createMoyenTransport(MoyenTransport moyenTransport);

  public MoyenTransport updateMoyenTransport(String id, MoyenTransport moyenTransport);

  public void deleteMoyenTransport(String id);
}
