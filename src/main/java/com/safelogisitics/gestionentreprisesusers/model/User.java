package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Field(value = "infosPerso")
  private InfosPerso infosPerso;
  
  @NotBlank
  @Field(value = "username")
  private String username;

  @NotBlank
  @JsonIgnore
  @Field(value = "password")
  private String password;

  @JsonIgnore
  @Field(value = "status")
  private int status;

  @NotBlank
  @Field(value = "dateCreation")
  private Date dateCreation;


  public User() {
  }

  public User(InfosPerso infosPerso, String username, String password, int status) {
    this.infosPerso = infosPerso;
    this.username = username;
    this.password = password;
    this.dateCreation = new Date();
    this.status = status;
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

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }
}
