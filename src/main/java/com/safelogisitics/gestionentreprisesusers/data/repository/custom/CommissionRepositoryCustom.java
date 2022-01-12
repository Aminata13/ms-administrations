package com.safelogisitics.gestionentreprisesusers.data.repository.custom;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommissionRepositoryCustom {
  
  public Page<CommissionModel> customSearch(CommissionSearchRequestDto commissionSearch, Pageable pageable);
}
