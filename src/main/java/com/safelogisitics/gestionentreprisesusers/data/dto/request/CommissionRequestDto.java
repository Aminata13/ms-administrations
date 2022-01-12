package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

public class CommissionRequestDto {
  
  @NotNull(message = "Le type service est obligatoire (LIVRAION, PRESTATION).")
  private EServiceType service;

  @NotBlank(message = "L'id de la commande est obligatoire.")
  private String commandeId;

  @NotBlank(message = "Le numero de la commande est obligatoire.")
  private String numero;

  @NotNull(message = "Le prix de la commande est obligatoire.")
  private BigDecimal prix;

  @NotBlank(message = "L'id du responsable est obligatoire.")
  private String responsableId;

  public CommissionRequestDto() {}

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getCommandeId() {
    return this.commandeId;
  }

  public void setCommandeId(String commandeId) {
    this.commandeId = commandeId;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public BigDecimal getPrix() {
    return this.prix;
  }

  public void setPrix(BigDecimal prix) {
    this.prix = prix;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

}
