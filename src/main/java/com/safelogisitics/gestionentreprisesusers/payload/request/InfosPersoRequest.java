package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

public class InfosPersoRequest {
  @NotBlank
  protected String prenom;

  @NotBlank
  protected String nom;

  protected String photoProfil;

  @NotBlank
  protected String email;

  @NotBlank
  protected String telephone;

  protected String adresse;

  protected LocalDate dateNaissance;

  protected String numeroPermis;

  @NotBlank
  protected String numeroPiece;

  public String getPrenom() {
    return this.prenom;
  }

  public String getNom() {
    return this.nom;
  }

  public String getPhotoProfil() {
    return this.photoProfil;
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

  public LocalDate getDateNaissance() {
    return this.dateNaissance;
  }

  public String getNumeroPermis() {
    return this.numeroPermis;
  }

  public String getNumeroPiece() {
    return this.numeroPiece;
  }
}
