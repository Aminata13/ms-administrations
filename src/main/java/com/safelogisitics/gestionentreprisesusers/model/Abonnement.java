package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "abonnements")
public class Abonnement {
  
  @Id
  private String id;

  @Field(value = "typeAbonnement")
  private TypeAbonnement typeAbonnement;

  @Field(value = "compte")
  private Compte compte;

  @Field(value = "numeroCarte")
  private String numeroCarte;

  @Field(value = "carteBloquer")
  private boolean carteBloquer;

  @Field(value = "solde")
  private BigDecimal solde;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Abonnement() {
    this.dateCreation = new Date();
  }

  public Abonnement(TypeAbonnement typeAbonnement, Compte compte) {
    this.typeAbonnement = typeAbonnement;
    this.compte = compte;
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

  public Compte getCompte() {
    return this.compte;
  }

  public void setCompte(Compte compte) {
    this.compte = compte;
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

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

}
