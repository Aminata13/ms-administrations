package com.safelogisitics.gestionentreprisesusers.data.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETypePartenariat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "entreprises")
public class Entreprise extends AuditMetadata {

  @Id
  private String id;

  @Field(value = "typeEntreprise")
  private String typeEntreprise;

  @Field(value = "domaineActivite")
  private String domaineActivite;

  @Field(value = "typePartenariats")
  private Set<ETypePartenariat> typePartenariats = new HashSet<>();

  @Field(value = "denomination")
  private String denomination;

  @Field(value = "ninea")
  private String ninea;

  @Field(value = "raisonSociale")
  private String raisonSociale;

  @Field(value = "gerant")
  private String gerantId;

  @Field(value = "email")
  private String email;

  @Field(value = "telephone")
  private String telephone;

  @Field(value = "adresse")
  private String adresse;

  @Field(value = "numeroCarte")
  private String numeroCarte;

  @JsonIgnore
  @Field(value = "deleted")
  private boolean deleted;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public Entreprise() {
    this.deleted = false;
    this.dateCreation = LocalDateTime.now();
  }

  public Entreprise(String typeEntreprise, String domaineActivite, String denomination, String ninea, String raisonSociale, String email, String telephone, String adresse) {
    this.typeEntreprise = typeEntreprise;
    this.domaineActivite = domaineActivite;
    this.denomination = denomination;
    this.ninea = ninea;
    this.raisonSociale = raisonSociale;
    this.email = email;
    this.telephone = telephone;
    this.adresse = adresse;
    this.deleted = false;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTypeEntreprise() {
    return this.typeEntreprise;
  }

  public void setTypeEntreprise(String typeEntreprise) {
    this.typeEntreprise = typeEntreprise;
  }

  public String getDomaineActivite() {
    return this.domaineActivite;
  }

  public void setDomaineActivite(String domaineActivite) {
    this.domaineActivite = domaineActivite;
  }

  public Set<ETypePartenariat> getTypePartenariats() {
    return this.typePartenariats;
  }

  public void setTypePartenariats(Set<ETypePartenariat> typePartenariats) {
    this.typePartenariats = typePartenariats;
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

  public String getRaisonSociale() {
    return this.raisonSociale;
  }

  public void setRaisonSociale(String raisonSociale) {
    this.raisonSociale = raisonSociale;
  }

  public String getGerantId() {
    return this.gerantId;
  }

  public void setGerantId(String gerantId) {
    this.gerantId = gerantId;
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

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public void setNumeroCarte(String numeroCarte) {
    this.numeroCarte = numeroCarte;
  }

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

  @JsonIgnore
  public Object getDefaultFields() {
    Object defaultFields = new Object() {
      public final String id = getId();
      public final String typeEntreprise = getTypeEntreprise();
      public final String domaineActivite = getDomaineActivite();
      public final String typePartenariats = getTypePartenariats().toString();
      public final String denomination = getDenomination();
      public final String ninea = getNinea();
      public final String raisonSociale = getRaisonSociale();
      public final String gerantId = getGerantId();
      public final String numeroCarte = getNumeroCarte();
      public final String email = getEmail();
      public final String telephone = getTelephone();
      public final String adresse = getAdresse();
      public final String dateCreation = getDateCreation().toString();

      @Override
      public String toString() {
        return String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s", id, typeEntreprise, domaineActivite, typePartenariats, denomination, ninea,
          raisonSociale, gerantId, numeroCarte, email, telephone, adresse, dateCreation); 
      }
    };
    return defaultFields;
  }
}
