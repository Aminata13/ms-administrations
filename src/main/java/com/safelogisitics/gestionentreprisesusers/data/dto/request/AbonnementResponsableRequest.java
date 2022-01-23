package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;

public class AbonnementResponsableRequest {

  @NotNull(message = "Le type de compte est obligatoire.")
  private ECompteType compteType;

  @NotBlank(message = "Le responsable est obligatoire.")
  private String responsableId;

  public AbonnementResponsableRequest() {}

  public ECompteType getCompteType() {
    return this.compteType;
  }

  public void setCompteType(ECompteType compteType) {
    this.compteType = compteType;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

}
