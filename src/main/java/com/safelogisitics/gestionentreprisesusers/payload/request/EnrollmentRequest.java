package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class EnrollmentRequest {

  @NotBlank
  protected String prenom;

  @NotBlank
  protected String nom;

  @NotBlank
  private String numeroCarte;

  @DecimalMin(value = "2000", message = "Montant invalide")
  private BigDecimal montant;

  protected String email;

  @NotBlank
  protected String telephone;

  protected String adresse;

  private LocalDate dateNaissance;

  private String numeroPiece;

  private String username;

  private String password;

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

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

  public LocalDate getDateNaissance() {
    return this.dateNaissance;
  }

  public String getNumeroPiece() {
    return this.numeroPiece;
  }

  public String getUsername() {
    return this.username.replaceAll("\\D+","");
  }

  public String getPassword() {
    return this.password;
  }

  public boolean isRegistrationDataValid() {
    return  (email != null && !email.isEmpty()) &&
      (username != null && !username.isEmpty()) &&
      (password != null && !password.isEmpty());
  }
}