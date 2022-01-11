package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.EntrepriseProspectRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.EntrepriseRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntrepriseService {

  public Page<Entreprise> getEntreprises(String typeEntreprise, String domaineActivite, String agentId, String denomination, String ninea, Pageable pageable);

  public Optional<Entreprise> getEntrepriseById(String id);

  public Entreprise createEntreprise(EntrepriseRequest request);

  public Entreprise createEntreprise(EntrepriseProspectRequest request);

  public Entreprise updateEntreprise(String id, EntrepriseRequest request);

  public void deleteEntreprise(String id);
}
