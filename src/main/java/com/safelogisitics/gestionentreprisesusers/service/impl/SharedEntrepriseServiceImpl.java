package com.safelogisitics.gestionentreprisesusers.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedEntrepriseModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.repository.SharedEntrepriseRepository;
import com.safelogisitics.gestionentreprisesusers.service.SharedEntrepriseService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SharedEntrepriseServiceImpl implements SharedEntrepriseService {

  private SharedEntrepriseRepository sharedEntrepriseRepository;

  private ObjectMapper objectMapper;

  public SharedEntrepriseServiceImpl(SharedEntrepriseRepository sharedEntrepriseRepository, ObjectMapper objectMapper) {
    this.sharedEntrepriseRepository = sharedEntrepriseRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public Page<SharedEntrepriseModel> getEntreprises(Pageable pageable) {
    return this.sharedEntrepriseRepository.findAll(pageable);
  }

  @Override
  public SharedEntrepriseModel createOrUpdateEntreprise(Entreprise entrepriseRequest) {
    SharedEntrepriseModel entreprise = null;

    if (this.sharedEntrepriseRepository.existsById(entrepriseRequest.getId())) {
      entreprise = this.sharedEntrepriseRepository.findById(entrepriseRequest.getId()).get();
    } else {
      entreprise = this.objectMapper.convertValue(entrepriseRequest, SharedEntrepriseModel.class);
    }

    this.sharedEntrepriseRepository.save(entreprise);

    return entreprise;
  }

  @Override
  public void deleteEntreprise(String infosPersoId) {
    this.sharedEntrepriseRepository.deleteById(infosPersoId);
  }
}
