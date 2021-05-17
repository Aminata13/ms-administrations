package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "entreprises")
public class Entreprise {
  
  @Id
  private String id;
  
  @Field(value = "denomination")
  private String denomination;

  @Field(value = "ninea")
  private String ninea;

  @Field(value = "email")
  private String email;

  @Field(value = "telephone")
  private String telephone;

  @Field(value = "adresse")
  private String adresse;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Entreprise() {
  }

  public Entreprise(String denomination, String ninea, String email, String telephone, String adresse) {
    this.denomination = denomination;
    this.ninea = ninea;
    this.email = email;
    this.telephone = telephone;
    this.adresse = adresse;
    this.dateCreation = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDenomination() {
    return this.denomination;
  }

  public void setDenomination(String denomination) {
    this.denomination = denomination;
  }

  public String getNinea() {
    return this.ninea;
  }

  public void setNinea(String ninea) {
    this.ninea = ninea;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getAdresse() {
    return this.adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }
}
