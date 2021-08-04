package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeEntreprise;

public class EntrepriseProspectRequest {
  
  @NotBlank
  private ETypeEntreprise typeEntreprise;

  @NotBlank
  private String denomination;

  private String ninea;

  private String raisonSociale;

  @NotBlank
  private String email;

  @NotBlank
  private String telephone;

  private String adresse;

  public ETypeEntreprise getTypeEntreprise() {
    return this.typeEntreprise;
  }

  public String getDenomination() {
    return this.denomination;
  }

  public String getNinea() {
    return this.ninea;
  }

  public String getRaisonSociale() {
    return this.raisonSociale;
  }

  public String getEmail() {
    return this.email;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public String getAdresse() {
    return this.adresse;
  }
}
