package com.safelogisitics.gestionentreprisesusers.data.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.google.firebase.database.annotations.NotNull;

public class FournitureEquipement extends AuditMetadata {

  @NotNull
  private BigDecimal cout;

  @NotNull
  private double quantite;

  @NotBlank
  private String fournisseur;

  @NotBlank
  private String numeroCommande;

  @NotNull
  private LocalDate dateAchat;

  private LocalDateTime dateCreation;

  public FournitureEquipement() {
    this.dateCreation = LocalDateTime.now();
  }

  public FournitureEquipement(BigDecimal cout, double quantite, String fournisseur, String numeroCommande, LocalDate dateAchat) {
    this.cout = cout;
    this.quantite = quantite;
    this.fournisseur = fournisseur;
    this.numeroCommande = numeroCommande;
    this.dateAchat = dateAchat;
    this.dateCreation = LocalDateTime.now();
  }

  public BigDecimal getCout() {
    return this.cout;
  }

  public void setCout(BigDecimal cout) {
    this.cout = cout;
  }

  public double getQuantite() {
    return this.quantite;
  }

  public void setQuantite(double quantite) {
    this.quantite = quantite;
  }

  public String getFournisseur() {
    return this.fournisseur;
  }

  public void setFournisseur(String fournisseur) {
    this.fournisseur = fournisseur;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public LocalDate getDateAchat() {
    return this.dateAchat;
  }

  public void setDateAchat(LocalDate dateAchat) {
    this.dateAchat = dateAchat;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

}
