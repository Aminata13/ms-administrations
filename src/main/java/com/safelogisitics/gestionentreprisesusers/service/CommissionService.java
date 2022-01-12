package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PayerCommissionsRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommissionService {
  
  public Page<CommissionModel> getListCommissions(CommissionSearchRequestDto commissionSearchRequest, Pageable pageable);

  public Optional<CommissionModel> getOneCommission(String id);

  public CommissionModel createCommission(CommissionRequestDto commissionRequest);

  public CommissionModel updateCommission(String id, CommissionRequestDto CommissionRequest);

  public PaiementCommissionModel payerCommissions(PayerCommissionsRequestDto payerCommissionsRequest);

  public void deleteCommission(String id);

  public void deleteCommissionByCommandeId(String id);

  public Page<PaiementCommissionModel> getListPaiementCommissions(CommissionSearchRequestDto commissionSearchRequest, Pageable pageable);
}
