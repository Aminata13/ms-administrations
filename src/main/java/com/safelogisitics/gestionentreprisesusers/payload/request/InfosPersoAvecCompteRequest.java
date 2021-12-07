package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceConciergeType;

public class InfosPersoAvecCompteRequest extends InfosPersoRequest {
  private String entrepriseId;

  private String roleId;

  private String numeroEmei;

  private String numeroReference;

  @NotNull(message = "Le statut est obligatoire.")
  private Integer statut;

  private EServiceConciergeType serviceConciergerie;

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

  public EServiceConciergeType getServiceConciergerie() {
    return this.serviceConciergerie;
  }

  public void setServiceConciergerie(EServiceConciergeType serviceConciergerie) {
    this.serviceConciergerie = serviceConciergerie;
  }

  public boolean valideFieldsCompteAgent() {
    if (
      (this.getNumeroEmei() == null || this.getNumeroEmei().isEmpty()) || (this.getNumeroReference() == null || this.getNumeroReference().isEmpty()) ||
      (this.getNumeroPermis() == null || this.getNumeroPermis().isEmpty()) || (this.getNumeroPiece() == null || this.getNumeroPiece().isEmpty())
    ) return false;

    return true;
  }
  
  public boolean valideFieldsComptePrestataire() {
    if (
      (this.getNumeroReference() == null || this.getNumeroReference().isEmpty()) || (this.getNumeroPiece() == null || this.getNumeroPiece().isEmpty()) ||
      this.getServiceConciergerie() == null
    ) return false;

    return true;
  }
}
