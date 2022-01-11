package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.SMSModelRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SMSRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SendSmsRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.SMSModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SMSService {

  public void sendSms(Set<SMSRequest> messages);

  public void sendSms(SendSmsRequest message);

  public Page<SMSModel> getSMSModels(Map<String, String>parameters, Pageable pageable);

  public Optional<SMSModel> getSMSModelById(String id);

  public SMSModel createSMSModel(SMSModelRequest SMSModelRequest);

  public SMSModel updateSMSModel(String id, SMSModelRequest SMSModelRequest);

  public void deleteSMSModel(String id);

  public Collection<String> getSMSData();

  public Collection<String> getSMSRepetitions();
}
