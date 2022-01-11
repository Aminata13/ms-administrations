package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class EnrollmentRequest {

  @NotBlank(message = "Le prénom est obligatoire.")
  protected String prenom;

  @NotBlank(message = "Le prénom est obligatoire.")
  protected String nom;

  @NotBlank(message = "Le numéro de carte est obligatoire.")
  private String numeroCarte;

  @DecimalMin(value = "2000", message = "Le montant est invalide.")
  private BigDecimal montant;

  protected String email;

  @NotBlank(message = "Le numéro de téléphone est obligatoire.")
  protected String telephone;

  protected String adresse;

  private LocalDate dateNaissance;

  private String anneeNaissance;

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
    return this.telephone.replaceAll("\\s+","");
  }

  public String getAdresse() {
    return this.adresse;
  }

  public LocalDate getDateNaissance() {
    return this.dateNaissance;
  }

  public String getAnneeNaissance() {
    return this.anneeNaissance;
  }

  public String getNumeroPiece() {
    return this.numeroPiece;
  }

  public String getUsername() {
    return this.username != null ? this.username.replaceAll("\\s+","") : this.username;
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