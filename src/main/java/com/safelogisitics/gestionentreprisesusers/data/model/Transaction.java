package com.safelogisitics.gestionentreprisesusers.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "transactions")
public class Transaction extends AuditMetadata {

  @Id
  private String id;

  @Field(value = "abonnement")
  private Abonnement abonnement;

  @Field(value = "reference")
  private String reference;

  @Field(value = "type")
  private ETransactionType type = ETransactionType.SOLDE_COMPTE;

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
  private BigDecimal montant = BigDecimal.valueOf(0);

  @Field(value = "points")
  private long points = 0;

  @Field(value = "nouveauSolde")
  private BigDecimal nouveauSolde = BigDecimal.valueOf(0);

  @Field(value = "totalPoints")
  private long totalPoints = 0;

  @Field(value = "approbation")
  private int approbation;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  @Field(value = "dateApprobation")
  private LocalDateTime dateApprobation;

  @Field(value = "initialFacture")
  private int initialFacture;

  @Field(value = "ordreFacture")
  private int ordreFacture;

  public Transaction() {
    this.dateCreation = LocalDateTime.now();
  }

  public Transaction(Abonnement abonnement, String reference, ETransactionAction action, Compte compteCreateur) {
    this.abonnement = abonnement;
    this.reference = reference;
    this.action = action;
    this.compteCreateur = compteCreateur;
    this.dateCreation = LocalDateTime.now();
  }

  public Transaction(Abonnement abonnement, String reference, ETransactionAction action, Compte compteCreateur, BigDecimal montant) {
    this.abonnement = abonnement;
    this.reference = reference;
    this.action = action;
    this.montant = montant;
    this.compteCreateur = compteCreateur;
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

  public ETransactionType getType() {
    return this.type;
  }

  public void setType(ETransactionType type) {
    this.type = type;
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

  public long getPoints() {
    return this.points;
  }

  public void setPoints(long points) {
    this.points = points;
  }

  public BigDecimal getNouveauSolde() {
    return this.nouveauSolde;
  }

  public void setNouveauSolde(BigDecimal nouveauSolde) {
    this.nouveauSolde = nouveauSolde;
  }

  public long getTotalPoints() {
    return this.totalPoints;
  }

  public void setTotalPoints(long totalPoints) {
    this.totalPoints = totalPoints;
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

  public int getInitialFacture() {
    return initialFacture;
  }

  public void setInitialFacture(int initialFacture) {
    this.initialFacture = initialFacture;
  }


  public int getOrdreFacture() {
    return ordreFacture;
  }

  public void setOrdreFacture(int ordreFacture) {
    this.ordreFacture = ordreFacture;
  }
}
