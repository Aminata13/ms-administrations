package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

public class InfosPersoRequest {
  
  private String id;

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

  protected String anneeNaissance;

  protected String numeroPermis;

  protected String numeroPiece;

  public String getId() {
    return this.id;
  }

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
    return this.telephone.replaceAll("\\s+","");
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

  public String getAnneeNaissance() {
    return this.anneeNaissance;
  }
}
