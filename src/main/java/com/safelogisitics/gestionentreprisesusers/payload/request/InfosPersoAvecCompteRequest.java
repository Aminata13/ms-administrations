package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotNull;

public class InfosPersoAvecCompteRequest extends InfosPersoRequest {
  private String entrepriseId;

  private String roleId;

  private String numeroEmei;

  @NotNull
  private int statut;

  public String getEntrepriseId() {
    return this.entrepriseId;
  }

  public String getRoleId() {
    return this.roleId;
  }

  public String getNumeroEmei() {
    return this.numeroEmei;
  }

  public int getStatut() {
    return this.statut;
  }
}
