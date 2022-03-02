package com.safelogisitics.gestionentreprisesusers.data.repository.custom;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfosPersoRepositoryCustom {
  
  public Page<InfosPersoModel> customSearch(InfosPersoSearchRequestDto InfosPersoSearch, Pageable pageable);
}
