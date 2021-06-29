package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeEntreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypePartenariat;
import com.safelogisitics.gestionentreprisesusers.payload.request.EntrepriseRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntrepriseService {

  public Page<Entreprise> getEntreprises(ETypeEntreprise typeEntreprise, Set<ETypePartenariat> typePartenariats, String agentId, String denomination, String ninea, Pageable pageable);

  public Optional<Entreprise> getEntrepriseById(String id);

  public Entreprise createEntreprise(EntrepriseRequest request);

  public Entreprise updateEntreprise(String id, EntrepriseRequest request);

  public void deleteEntreprise(String id);
}