package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

import com.safelogisitics.gestionentreprisesusers.data.model.enums.EMoyenTransportType;

public class MoyenTransportSearchDto {
  
  private EMoyenTransportType type;

  private String reference;

  private String marque;

  private String modele;

  private String numeroSerie;

  private String numeroCarteGrise;

  private String fournisseur;

  private String matricule;

  private String numeroCommande;

  private String enService;

  private String numeroCarteTotal;

  public EMoyenTransportType getType() {
    return this.type;
  }

  public void setType(EMoyenTransportType type) {
    this.type = type;
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getMarque() {
    return this.marque;
  }

  public void setMarque(String marque) {
    this.marque = marque;
  }

  public String getModele() {
    return this.modele;
  }

  public void setModele(String modele) {
    this.modele = modele;
  }

  public String getNumeroSerie() {
    return this.numeroSerie;
  }

  public void setNumeroSerie(String numeroSerie) {
    this.numeroSerie = numeroSerie;
  }

  public String getNumeroCarteGrise() {
    return this.numeroCarteGrise;
  }

  public void setNumeroCarteGrise(String numeroCarteGrise) {
    this.numeroCarteGrise = numeroCarteGrise;
  }

  public String getFournisseur() {
    return this.fournisseur;
  }

  public void setFournisseur(String fournisseur) {
    this.fournisseur = fournisseur;
  }

  public String getMatricule() {
    return this.matricule;
  }

  public void setMatricule(String matricule) {
    this.matricule = matricule;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public String getEnService() {
    return this.enService;
  }

  public void setEnService(String enService) {
    this.enService = enService;
  }

  public String getNumeroCarteTotal() {
    return this.numeroCarteTotal;
  }

  public void setNumeroCarteTotal(String numeroCarteTotal) {
    this.numeroCarteTotal = numeroCarteTotal;
  }

}
