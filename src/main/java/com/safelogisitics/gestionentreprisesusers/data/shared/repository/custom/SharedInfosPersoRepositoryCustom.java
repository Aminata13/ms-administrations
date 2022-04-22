package com.safelogisitics.gestionentreprisesusers.data.shared.repository.custom;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedInfosPersoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedInfosPersoRepositoryCustom {
  
  public Page<SharedInfosPersoModel> customSearch(InfosPersoSearchRequestDto InfosPersoSearch, Pageable pageable);
}
