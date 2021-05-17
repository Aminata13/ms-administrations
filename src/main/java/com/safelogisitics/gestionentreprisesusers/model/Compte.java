package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;

import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comptes")
public class Compte {

  @Id
  private String id;

  @Field(value = "type")
  private ECompteType type;

  @Field(value = "infosPerso")
  private InfosPerso infosPerso;

  @Field(value = "entreprise")
  private Entreprise entreprise;

  @Field(value = "statut")
  private int statut;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Compte() {
  }

  public Compte(ECompteType type, InfosPerso infosPerso, String email, String telephone, int statut) {
    this.type = type;
    this.infosPerso = infosPerso;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, Entreprise entreprise, InfosPerso infosPerso, String email, String telephone, int statut) {
    this.type = type;
    this.entreprise = entreprise;
    this.infosPerso = infosPerso;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
