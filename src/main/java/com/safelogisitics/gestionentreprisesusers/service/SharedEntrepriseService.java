package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedEntrepriseModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedEntrepriseService {

  public Page<SharedEntrepriseModel> getEntreprises(Pageable pageable);

  public SharedEntrepriseModel createOrUpdateEntreprise(Entreprise entrepriseRequest);

  public void deleteEntreprise(String infosPersoId); 
}
