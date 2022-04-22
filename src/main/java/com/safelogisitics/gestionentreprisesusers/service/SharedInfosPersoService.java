package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.shared.dto.SharedInfosPersoRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedInfosPersoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedInfosPersoService {

  public Page<SharedInfosPersoModel> getInfosPersos(Pageable pageable);

  public SharedInfosPersoModel createOrUpdateInfosPerso(SharedInfosPersoRequestDto infosPersoRequest);

  public SharedInfosPersoModel convertToSharedInfosPersoRequestAndSave(String infosPersoId);

  public void deleteInfosPerso(String infosPersoId); 
}
