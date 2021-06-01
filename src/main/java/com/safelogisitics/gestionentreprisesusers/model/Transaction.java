package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.util.Date;

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

  @Field(value = "compteCreateur")
  private Compte compteCreateur;

  @Field(value = "montant")
  private BigDecimal montant;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Transaction() {
    this.dateCreation = new Date();
  }

  public Transaction(Abonnement abonnement, String reference, ETransactionAction action, Compte compteCreateur, BigDecimal montant) {
    this.abonnement = abonnement;
    this.reference = reference;
    this.action = action;
    this.compteCreateur = compteCreateur;
    this.montant = montant;
    this.dateCreation = new Date();
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

  public Compte getCompteCreateur() {
    return this.compteCreateur;
  }

  public void setCompteCreateur(Compte compteCreateur) {
    this.compteCreateur = compteCreateur;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

}
