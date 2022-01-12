package com.safelogisitics.gestionentreprisesusers.data.repository.custom;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaiementCommissionRepositoryCustom {
  
  public Page<PaiementCommissionModel> customSearch(CommissionSearchRequestDto commissionSearch, Pageable pageable);
}
