package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "transactions")
public class Transaction {

  @Id
  private String id;

  @Field(value = "abonnement")
  private Abonnement abonnement;

  @Field(value = "reference")
  private String reference;

  @Field(value = "action")
  private ETransactionAction action;

  @Field(value = "numeroCommande")
  private String numeroCommande;

  private EServiceType service;

  @Field(value = "compteCreateur")
  private Compte compteCreateur;

  @Field(value = "approbateur")
  private Compte approbateur;

  @Field(value = "montant")
  private BigDecimal montant;

  @Field(value = "nouveauSolde")
  private BigDecimal nouveauSolde;

  @Field(value = "approbation")
  private int approbation;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  @Field(value = "dateApprobation")
  private LocalDateTime dateApprobation;

  public Transaction() {
    this.dateCreation = LocalDateTime.now();
  }

  public Transaction(Abonnement abonnement, String reference, ETransactionAction action, Compte compteCreateur, BigDecimal montant) {
    this.abonnement = abonnement;
    this.reference = reference;
    this.action = action;
    this.compteCreateur = compteCreateur;
    this.montant = montant;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Abonnement getAbonnement() {
    return this.abonnement;
  }

  public void setAbonnement(Abonnement abonnement) {
    this.abonnement = abonnement;
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public ETransactionAction getAction() {
    return this.action;
  }

  public void setAction(ETransactionAction action) {
    this.action = action;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public Compte getCompteCreateur() {
    return this.compteCreateur;
  }

  public void setCompteCreateur(Compte compteCreateur) {
    this.compteCreateur = compteCreateur;
  }

  public Compte getApprobateur() {
    return this.approbateur;
  }

  public void setApprobateur(Compte approbateur) {
    this.approbateur = approbateur;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public BigDecimal getNouveauSolde() {
    return this.nouveauSolde;
  }

  public void setNouveauSolde(BigDecimal nouveauSolde) {
    this.nouveauSolde = nouveauSolde;
  }

  public int getApprobation() {
    return this.approbation;
  }

  public void setApprobation(int approbation) {
    this.approbation = approbation;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

  public LocalDateTime getDateApprobation() {
    return this.dateApprobation;
  }

  public void setDateApprobation(LocalDateTime dateApprobation) {
    this.dateApprobation = dateApprobation;
  }
}
