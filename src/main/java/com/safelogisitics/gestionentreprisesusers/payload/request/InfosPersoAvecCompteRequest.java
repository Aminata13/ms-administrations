package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotNull;

public class InfosPersoAvecCompteRequest extends InfosPersoRequest {
  private String entrepriseId;

  private String roleId;

  private String numeroEmei;

  private String numeroReference;

  @NotNull
  private int statut;

  public String getEntrepriseId() {
    return this.entrepriseId;
  }

  public void setEntrepriseId(String entrepriseId) {
    this.entrepriseId = entrepriseId;
  }

  public String getRoleId() {
    return this.roleId;
  }

  public String getNumeroEmei() {
    return this.numeroEmei;
  }

  public String getNumeroReference() {
    return this.numeroReference;
  }

  public int getStatut() {
    return this.statut;
  }

  public boolean valideFieldsCompteAgent() {
    if (
      (this.getNumeroEmei() == null || this.getNumeroEmei().isEmpty()) || (this.getNumeroReference() == null || this.getNumeroReference().isEmpty()) ||
      (this.getNumeroPermis() == null || this.getNumeroPermis().isEmpty()) || (this.getNumeroPiece() == null || this.getNumeroPiece().isEmpty())
    ) return false;

    return true;
  }
}
