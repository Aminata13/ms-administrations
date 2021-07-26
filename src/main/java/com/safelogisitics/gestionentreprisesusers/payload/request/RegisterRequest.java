package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

public class RegisterRequest extends InfosPersoRequest {

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  public RegisterRequest(String prenom, String nom, String email, String telephone, String username, String password, String adresse, LocalDate dateNaissance, String numeroPermis, String numeroPiece, String photoProfil) {
    this.prenom = prenom;
    this.nom = nom;
    this.email = email;
    this.telephone = telephone;
    this.username = username;
    this.password = password;
    this.adresse = adresse;
    this.dateNaissance = dateNaissance;
    this.numeroPermis = numeroPermis;
    this.numeroPiece = numeroPiece;
    this.photoProfil = photoProfil;
  }

  public String getUsername() {
    return this.username.replaceAll("\\s+","");
  }

  public String getPassword() {
    return this.password;
  }
}
