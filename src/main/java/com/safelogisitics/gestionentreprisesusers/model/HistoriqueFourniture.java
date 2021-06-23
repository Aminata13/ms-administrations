package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class HistoriqueFourniture {

  private BigDecimal cout;

  private double quantite;

  private String fournisseur;

  private String numeroCommande;

  private LocalDate dateAchat;

  private LocalDateTime dateCreation;

  public HistoriqueFourniture() {
    this.dateCreation = LocalDateTime.now();
  }

  public HistoriqueFourniture(BigDecimal cout, double quantite, String fournisseur, String numeroCommande, LocalDate dateAchat) {
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
