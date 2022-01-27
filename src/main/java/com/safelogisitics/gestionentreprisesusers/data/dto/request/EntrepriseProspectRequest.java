package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import javax.validation.constraints.NotBlank;

public class EntrepriseProspectRequest {
  
  @NotBlank
  private String typeEntreprise;

  @NotBlank
  private String domaineActivite;

  @NotBlank
  private String denomination;

  private String ninea;

  private String raisonSociale;

  @NotBlank
  private String email;

  @NotBlank
  private String telephone;

  private String adresse;

  private String logo;

  public String getTypeEntreprise() {
    return this.typeEntreprise;
  }

  public String getDomaineActivite() {
    return this.domaineActivite;
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
    return this.telephone.replaceAll("\\s+","");
  }

  public String getAdresse() {
    return this.adresse;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }
}
