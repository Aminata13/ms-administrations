package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.model.AuditMetadata;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entreprises")
@TypeAlias("SharedEntrepriseModel")
public class SharedEntrepriseModel extends AuditMetadata {

  @Id
  private String id;

  private String typeEntreprise;

  private String domaineActivite;

  private Set<String> typePartenariats;

  private String denomination;

  private String ninea;

  private String raisonSociale;

  private String gerantId;

  private String email;

  private String telephone;

  private String mobile;

  private String adresse;

  private String numeroCarte;

  @JsonIgnore
  private boolean deleted;

  private LocalDateTime dateCreation;

  private String logo;

  public SharedEntrepriseModel() {}

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

  public Set<String> getTypePartenariats() {
    return this.typePartenariats;
  }

  public void setTypePartenariats(Set<String> typePartenariats) {
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

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
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

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }
}
