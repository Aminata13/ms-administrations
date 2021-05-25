package com.safelogisitics.gestionentreprisesusers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

  @Indexed
  @Field(value = "email")
  private String email;

  @Indexed
  @Field(value = "telephone")
  private String telephone;

  @Field(value = "adresse")
  private String adresse;

  @DBRef
  @Field(value = "comptes")
  private Set<Compte> comptes = new HashSet<>();

  @Field(value = "dateCreation")
  private Date dateCreation;

  public InfosPerso() {
    this.dateCreation = new Date();
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

  public void addCompte(Compte compte) {
    if (!this.comptes.contains(compte)) {
      this.comptes.add(compte);
    }
  }

  public void updateCompte(Compte compte) {
    if (this.comptes.contains(compte)) {
      this.comptes.remove(compte);
    }
    this.comptes.add(compte);
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  @JsonIgnore
  public List<String> getTypeAndPrivileges() {
    List<String> authorities = new ArrayList<>();

    for (Compte compte : comptes) {
      if (compte.isDeleted()) {
        continue;
      }

      authorities.add(compte.getType().name());

      if (compte.getRole() == null || compte.getRole().getPrivileges().isEmpty()) {
        continue;
      }
      for (Privilege privilege : compte.getRole().getPrivileges()) {
        authorities.add(privilege.getValeur().name());
      }
    }

    return authorities;
  }

  @JsonIgnore
  public Object getDefaultFields() {
    Object defaultFields = new Object() {
      public final String id = getId();
      public final String prenom = getPrenom();
      public final String nom = getNom();
      public final String email = getEmail();
      public final String telephone = getTelephone();
      public final String adresse = getAdresse();
      public final String dateCreation = getDateCreation().toString();

      @Override
      public String toString() {
        return String.format("%s %s %s %s %s %s %s", id, prenom, nom, email, telephone, adresse, dateCreation); 
      }
    };
    return defaultFields;
  }
}
