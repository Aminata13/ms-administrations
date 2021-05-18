package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {
  @Id
  private String id;

  @DBRef
  @Field(value = "infosPerso")
  private InfosPerso infosPerso;

  @Field(value = "username")
  private String username;

  @JsonIgnore
  @Field(value = "password")
  private String password;

  @Field(value = "statut")
  private int statut;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public User() {
  }

  public User(InfosPerso infosPerso, String username, String password, int statut) {
    this.infosPerso = infosPerso;
    this.username = username;
    this.password = password;
    this.dateCreation = new Date();
    this.statut = statut;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InfosPerso getInfosPerso() {
    return infosPerso;
  }

  public void setInfosPerso(InfosPerso infosPerso) {
    this.infosPerso = infosPerso;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }
}
