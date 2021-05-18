package com.safelogisitics.gestionentreprisesusers.model;

import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.EPrivilege;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "privileges")
public class Privilege {
  
  @Id
  private String id;
  
  @Field(value = "libelle")
  private String libelle;

  @Field(value = "valeur")
  private EPrivilege valeur;

  @Field(value = "type")
  private ECompteType type;

  @Field(value = "statut")
  private int statut;

  public Privilege() {
  }

  public Privilege(String libelle, EPrivilege valeur, ECompteType type, int statut) {
    this.libelle = libelle;
    this.valeur = valeur;
    this.type = type;
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

  public EPrivilege getValeur() {
    return this.valeur;
  }

  public void setValeur(EPrivilege valeur) {
    this.valeur = valeur;
  }

  public ECompteType getType() {
    return this.type;
  }

  public void setType(ECompteType type) {
    this.type = type;
  }
  
  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }
}
