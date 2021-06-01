package com.safelogisitics.gestionentreprisesusers.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "typeAbonnements")
public class TypeAbonnement {
  
  @Id
  private String id;

  @Field(value = "libelle")
  private String libelle;

  @Field(value = "reduction")
  private int reduction;

  @Field(value = "statut")
  private int statut;

  public TypeAbonnement() {}

  public TypeAbonnement(String libelle, int reduction, int statut) {
    this.libelle = libelle;
    this.reduction = reduction;
    this.statut = statut;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public int getReduction() {
    return this.reduction;
  }

  public void setReduction(int reduction) {
    this.reduction = reduction;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

}
