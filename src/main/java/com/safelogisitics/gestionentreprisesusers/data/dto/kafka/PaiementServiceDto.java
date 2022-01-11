package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

import org.springframework.stereotype.Component;

@Component
public class PaiementServiceDto {

  private EServiceType service;

  private String serviceId;

  private String serviceReference;

  private String clientId;

  private String annulateurId;


  public PaiementServiceDto() {}

  public PaiementServiceDto(EServiceType service, String serviceId, String clientId) {
    this.service = service;
    this.serviceId = serviceId;
    this.clientId = clientId;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getServiceId() {
    return this.serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getServiceReference() {
    return this.serviceReference;
  }

  public void setServiceReference(String serviceReference) {
    this.serviceReference = serviceReference;
  }

  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getAnnulateurId() {
    return this.annulateurId;
  }

  public void setAnnulateurId(String annulateurId) {
    this.annulateurId = annulateurId;
  }

}
