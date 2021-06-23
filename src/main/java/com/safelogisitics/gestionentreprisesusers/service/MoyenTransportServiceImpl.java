package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.model.enums.EMoyenTransportType;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

public class MoyenTransportServiceImpl implements MoyenTransportService {

  public Collection<MoyenTransport> getMoyenTransports(String fournisseur, EMoyenTransportType type, String reference, String marque, String modele, Pageable pageable) {
    return null;
  }

  public Optional<MoyenTransport> getMoyenTransportById(String id) {
    return null;
  }

  public MoyenTransport createMoyenTransport(MoyenTransport moyenTransport) {
    return null;
  }

  public MoyenTransport updateMoyenTransport(String id, MoyenTransport moyenTransport) {
    return null;
  }

  public void deleteMoyenTransport(String id) {
    return;
  }
}
