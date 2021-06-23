package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.EMoyenTransportType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "moyenTransports")
public class MoyenTransport {
  
  @Id
  private String id;

  @Field(value = "type")
  private EMoyenTransportType type;

  @Field(value = "reference")
  private String reference;

  @Field(value = "marque")
  private String marque;

  @Field(value = "modele")
  private String modele;

  @Field(value = "numeroSerie")
  private String numeroSerie;

  @Field(value = "numeroCarteGrise")
  private String numeroCarteGrise;

  @Field(value = "fournisseur")
  private String fournisseur;

  @Field(value = "accessoires")
  private Set<String> accessoires = new HashSet<>();

  @Field(value = "matricule")
  private String matricule;

  @Field(value = "distanceParcourus")
  private double distanceParcourus;

  @Field(value = "nombreIncidents")
  private int nombreIncidents = 0;

  @Field(value = "dureeVie")
  private String dureeVie;

  @Field(value = "numeroCommande")
  private String numeroCommande;

  @Field(value = "numeroOrdreAjout")
  private String numeroOrdreAjout;

  @Field(value = "cout")
  private BigDecimal cout;

  @Field(value = "statut")
  private int statut;

  @Field(value = "enService")
  private boolean enService;

  @Field(value = "numeroCarteTotal")
  private String numeroCarteTotal;

  @Field(value = "dateAssurance")
  private LocalDate dateAssurance;

  @Field(value = "dateReception")
  private LocalDate dateReception;

  @Field(value = "dateMiseService")
  private LocalDate dateMiseService;

  @Field(value = "dateAchat")
  private LocalDate dateAchat;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public MoyenTransport() {
    this.dateCreation = LocalDateTime.now();
  }

  public MoyenTransport(
    EMoyenTransportType type, String marque, String modele, String numeroSerie, String fournisseur, Set<String> accessoires, double distanceParcourus,
    int nombreIncidents, String dureeVie, String numeroCommande, String numeroOrdreAjout, BigDecimal cout, LocalDate dateAchat, LocalDate dateReception
  ) {
    this.type = type;
    this.marque = marque;
    this.modele = modele;
    this.numeroSerie = numeroSerie;
    this.fournisseur = fournisseur;
    this.accessoires = accessoires;
    this.distanceParcourus = distanceParcourus;
    this.nombreIncidents = nombreIncidents;
    this.dureeVie = dureeVie;
    this.numeroCommande = numeroCommande;
    this.numeroOrdreAjout = numeroOrdreAjout;
    this.cout = cout;
    this.dateAchat = dateAchat;
    this.dateReception = dateReception;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public Set<String> getAccessoires() {
    return this.accessoires;
  }

  public void setAccessoires(Set<String> accessoires) {
    this.accessoires = accessoires;
  }

  public String getMatricule() {
    return this.matricule;
  }

  public void setMatricule(String matricule) {
    this.matricule = matricule;
  }

  public double getDistanceParcourus() {
    return this.distanceParcourus;
  }

  public void setDistanceParcourus(double distanceParcourus) {
    this.distanceParcourus = distanceParcourus;
  }

  public int getNombreIncidents() {
    return this.nombreIncidents;
  }

  public void setNombreIncidents(int nombreIncidents) {
    this.nombreIncidents = nombreIncidents;
  }

  public String getDureeVie() {
    return this.dureeVie;
  }

  public void setDureeVie(String dureeVie) {
    this.dureeVie = dureeVie;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public String getNumeroOrdreAjout() {
    return this.numeroOrdreAjout;
  }

  public void setNumeroOrdreAjout(String numeroOrdreAjout) {
    this.numeroOrdreAjout = numeroOrdreAjout;
  }

  public BigDecimal getCout() {
    return this.cout;
  }

  public void setCout(BigDecimal cout) {
    this.cout = cout;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public boolean isEnService() {
    return this.enService;
  }

  public void setEnService(boolean enService) {
    this.enService = enService;
  }

  public String getNumeroCarteTotal() {
    return this.numeroCarteTotal;
  }

  public void setNumeroCarteTotal(String numeroCarteTotal) {
    this.numeroCarteTotal = numeroCarteTotal;
  }

  public LocalDate getDateAssurance() {
    return this.dateAssurance;
  }

  public void setDateAssurance(LocalDate dateAssurance) {
    this.dateAssurance = dateAssurance;
  }

  public LocalDate getDateReception() {
    return this.dateReception;
  }

  public void setDateReception(LocalDate dateReception) {
    this.dateReception = dateReception;
  }

  public LocalDate getDateMiseService() {
    return this.dateMiseService;
  }

  public void setDateMiseService(LocalDate dateMiseService) {
    this.dateMiseService = dateMiseService;
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
