package com.safelogisitics.gestionentreprisesusers.service.impl;

import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.dto.SharedInfosPersoRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedInfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.repository.SharedInfosPersoRepository;
import com.safelogisitics.gestionentreprisesusers.service.SharedInfosPersoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SharedInfosPersoServiceImpl implements SharedInfosPersoService {

  private SharedInfosPersoRepository infosPersoRepository;

  private InfosPersoDao infosPersoDao;

  private CompteDao compteDao;

  private AbonnementDao abonnementDao;

  private ObjectMapper objectMapper;

  public SharedInfosPersoServiceImpl(
    SharedInfosPersoRepository infosPersoRepository,
    InfosPersoDao infosPersoDao,
    CompteDao compteDao,
    AbonnementDao abonnementDao,
    ObjectMapper objectMapper
  ) {
    this.infosPersoRepository = infosPersoRepository;
    this.infosPersoDao = infosPersoDao;
    this.compteDao = compteDao;
    this.abonnementDao = abonnementDao;
    this.objectMapper = objectMapper;
  }

  @Override
  public Page<SharedInfosPersoModel> getInfosPersos(Pageable pageable) {
    return this.infosPersoRepository.findAll(pageable);
  }

  @Override
  public SharedInfosPersoModel createOrUpdateInfosPerso(SharedInfosPersoRequestDto infosPersoRequest) {
    SharedInfosPersoModel infosPerso = this.objectMapper.convertValue(
      infosPersoRequest.getInfosPerso(), 
      SharedInfosPersoModel.class
    );
  
    if (infosPersoRequest.getAbonnement() != null && infosPersoRequest.getAbonnement().getId() != null) {
      infosPerso.setAbonnement(objectMapper, infosPersoRequest.getAbonnement());
    }

    if (infosPersoRequest.getComptes() != null && !infosPersoRequest.getComptes().isEmpty()) {
      infosPerso.setComptes(objectMapper, infosPersoRequest.getComptes());
    }

    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public SharedInfosPersoModel convertToSharedInfosPersoRequestAndSave(String infosPersoId) {
    if (!this.infosPersoDao.existsById(infosPersoId) || !this.compteDao.existsByInfosPersoId(infosPersoId)) {
      return null;
    }

    InfosPersoModel infosPerso = this.infosPersoDao.findById(infosPersoId).get();

    SharedInfosPersoRequestDto infosPersoRequest = new SharedInfosPersoRequestDto();

    infosPersoRequest.setInfosPerso(infosPerso);
    infosPersoRequest.setComptes(new HashSet<>(this.compteDao.findByInfosPersoId(infosPerso.getId())));

    if (this.abonnementDao.existsByCompteClientInfosPersoId(infosPerso.getId())) {
      infosPersoRequest.setAbonnement(this.abonnementDao.findByCompteClientInfosPersoId(infosPerso.getId()).get());
    }

    return this.createOrUpdateInfosPerso(infosPersoRequest);
  }

  @Override
  public void deleteInfosPerso(String infosPersoId) {
    this.infosPersoRepository.deleteById(infosPersoId);
  }
}
