package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.model.enums.EProspecteurType;
import com.safelogisitics.gestionentreprisesusers.payload.request.ProspectRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProspectService {

  public Page<Map<String, Object>> getProspects(Map<String,String> parameters, Pageable pageable);

  public Map<String, Object> getProspectById(String id);

  public Map<String, Object> createProspect(ProspectRequest prospectRequest, EProspecteurType prospecteurType);

  public Map<String, Object> updateProspect(String id, ProspectRequest prospectRequest, boolean force);

  public Map<String, Object> enroleProspect(String id, ProspectRequest prospectRequest);

  public void deleteProspect(String id);
}
