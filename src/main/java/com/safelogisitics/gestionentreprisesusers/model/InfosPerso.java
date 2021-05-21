package com.safelogisitics.gestionentreprisesusers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "infosPersos")
public class InfosPerso {

  @Id
  private String id;
  
  @Field(value = "prenom")
  private String prenom;

  @Field(value = "nom")
  private String nom;

  @Field(value = "email")
  private String email;

  @Field(value = "telephone")
  private String telephone;

  @Field(value = "adresse")
  private String adresse;

  @Field(value = "comptes")
  private Set<Compte> comptes = new HashSet<>();

  @Field(value = "dateCreation")
  private Date dateCreation;

  public InfosPerso() {
  }

  public InfosPerso(String prenom, String nom, String email, String telephone, String adresse) {
    this.prenom = prenom;
    this.nom = nom;
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

  public String getPrenom() {
    return this.prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
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

  public Set<Compte> getComptes() {
    return this.comptes;
  }

  public void setComptes(Set<Compte> comptes) {
    this.comptes = comptes;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public List<String> getTypeAndPrivileges() {

    List<String> _authorities = new ArrayList<>();

    for (Compte compte : comptes) {
      _authorities.add(compte.getType().name());
      if (compte.getRole() != null) {
        for (Privilege privilege: compte.getRole().getPrivileges()) {
          _authorities.add(privilege.getValeur().name());
        }
      }
    }

    return _authorities;
  } 
}
