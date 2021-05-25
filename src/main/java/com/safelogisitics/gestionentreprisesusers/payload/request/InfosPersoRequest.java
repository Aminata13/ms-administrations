package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class InfosPersoRequest {
  @NotBlank
  protected String prenom;

  @NotBlank
  protected String nom;

  @NotBlank
  protected String email;

  @NotBlank
  protected String telephone;

  @NotBlank
  protected String adresse;

  public String getPrenom() {
    return this.prenom;
  }

  public String getNom() {
    return this.nom;
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
