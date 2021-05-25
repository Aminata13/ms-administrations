package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class InfosPersoAvecCompteRequest extends InfosPersoRequest {
  @NotBlank
  private String entrepriseId;

  @NotBlank
  private String roleId;

  @NotNull
  private int statut;

  public String getEntrepriseId() {
    return this.entrepriseId;
  }

  public String getRoleId() {
    return this.roleId;
  }

  public int getStatut() {
    return this.statut;
  }
}
