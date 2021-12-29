package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceConciergeType;

public class InfosPersoAvecCompteRequest extends InfosPersoRequest {
  private String entrepriseId;

  private String roleId;

  private String numeroEmei;

  private String numeroReference;

  @NotNull(message = "Le statut est obligatoire.")
  private int statut;

  private Set<EServiceConciergeType> serviceConciergeries;

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

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public Set<EServiceConciergeType> getServiceConciergeries() {
    return this.serviceConciergeries;
  }

  public void setServiceConciergeries(Set<EServiceConciergeType> serviceConciergeries) {
    this.serviceConciergeries = serviceConciergeries;
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
      this.getServiceConciergeries() == null || this.getServiceConciergeries().isEmpty()
    ) return false;

    return true;
  }
}
