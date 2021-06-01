package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "abonnements")
public class Abonnement {

  @Id
  private String id;

  @Field(value = "typeAbonnement")
  private TypeAbonnement typeAbonnement;

  @Field(value = "compteClient")
  private Compte compteClient;

  @Field(value = "compteCreateur")
  private Compte compteCreateur;

  @Field(value = "numeroCarte")
  private String numeroCarte;

  @Field(value = "carteBloquer")
  private boolean carteBloquer;

  @Field(value = "solde")
  private BigDecimal solde;

  @Field(value = "statut")
  private int statut;

  @JsonIgnore
  @Field(value = "deleted")
  private boolean deleted;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Abonnement() {
    this.deleted = false;
    this.dateCreation = new Date();
  }

  public Abonnement(TypeAbonnement typeAbonnement, Compte compteClient, Compte compteCreateur, int statut) {
    this.typeAbonnement = typeAbonnement;
    this.compteClient = compteClient;
    this.compteCreateur = compteCreateur;
    this.statut = statut;
    this.deleted = false;
    this.dateCreation = new Date();
  }


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TypeAbonnement getTypeAbonnement() {
    return this.typeAbonnement;
  }

  public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
    this.typeAbonnement = typeAbonnement;
  }

  public Compte getCompteClient() {
    return this.compteClient;
  }

  public void setCompteClient(Compte compteClient) {
    this.compteClient = compteClient;
  }

  public Compte getCompteCreateur() {
    return this.compteCreateur;
  }

  public void setCompteCreateur(Compte compteCreateur) {
    this.compteCreateur = compteCreateur;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public void setNumeroCarte(String numeroCarte) {
    this.numeroCarte = numeroCarte;
  }

  public boolean isCarteBloquer() {
    return this.carteBloquer;
  }

  public void setCarteBloquer(boolean carteBloquer) {
    this.carteBloquer = carteBloquer;
  }

  public BigDecimal getSolde() {
    return this.solde;
  }

  public void setSolde(BigDecimal solde) {
    this.solde = solde;
  }

  public void rechargerCarte(BigDecimal montant) {
    this.solde.add(montant);
  }

  public void debiterCarte(BigDecimal montant) {
    this.solde.subtract(montant);
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

}
