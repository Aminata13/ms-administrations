package com.safelogisitics.gestionentreprisesusers.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @Field(value = "photoProfil")
  private String photoProfil;

  @Indexed
  @Field(value = "email")
  private String email;

  @Indexed
  @Field(value = "telephone")
  private String telephone;

  @Field(value = "adresse")
  private String adresse;

  @Field(value = "dateNaissance")
  private LocalDate dateNaissance;

  @Indexed
  @Field(value = "numeroPermis")
  private String numeroPermis;

  @Indexed
  @Field(value = "numeroPiece")
  private String numeroPiece;

  @Field(value = "statutProspect")
  private int statutProspect;

  @Field(value = "prospecteur")
  private String prospecteur;

  @DBRef
  @Field(value = "comptes")
  private Set<Compte> comptes = new HashSet<>();

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public InfosPerso() {
    this.dateCreation = LocalDateTime.now();
  }

  public InfosPerso(String prenom, String nom, String email, String telephone, String adresse, LocalDate dateNaissance, String numeroPermis, String numeroPiece, String photoProfil) {
    this.prenom = prenom;
    this.nom = nom;
    this.email = email;
    this.telephone = telephone;
    this.adresse = adresse;
    this.dateNaissance = dateNaissance;
    this.numeroPermis = numeroPermis;
    this.numeroPiece = numeroPiece;
    this.photoProfil = photoProfil;
    this.dateCreation = LocalDateTime.now();
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

  public String getPhotoProfil() {
    return this.photoProfil;
  }

  public void setPhotoProfil(String photoProfil) {
    this.photoProfil = photoProfil;
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

  public LocalDate getDateNaissance() {
    return this.dateNaissance;
  }

  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public String getNumeroPermis() {
    return this.numeroPermis;
  }

  public void setNumeroPermis(String numeroPermis) {
    this.numeroPermis = numeroPermis;
  }

  public String getNumeroPiece() {
    return this.numeroPiece;
  }

  public void setNumeroPiece(String numeroPiece) {
    this.numeroPiece = numeroPiece;
  }

  public int getStatutProspect() {
    return this.statutProspect;
  }

  public void setStatutProspect(int statutProspect) {
    this.statutProspect = statutProspect;
  }

  public String getProspecteur() {
    return this.prospecteur;
  }

  public void setProspecteur(String prospecteur) {
    this.prospecteur = prospecteur;
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

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

  @JsonIgnore
  public List<String> getTypeAndPrivileges() {
    List<String> authorities = new ArrayList<>();

    for (Compte compte : comptes) {
      if (compte.isDeleted()) {
        continue;
      }

      authorities.add("ROLE_"+compte.getType().name());

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
