package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Criteria;

public class InfosPersoSearchRequestDto {

  private Set<String> ids;
  
  private String prenom;

  private String nom;

  private String email;

  private String username;

  private String telephone;

  private String adresse;

  private LocalDate dateNaissance;

  private String anneeNaissance;

  private String numeroPermis;

  private String numeroPiece;

  private CompteSearchRequestDto CompteSearch = new CompteSearchRequestDto();

  private AbonnementSearchRequestDto abonnementSearch;

  public InfosPersoSearchRequestDto() {}

  public Set<String> getIds() {
    return this.ids;
  }

  public void setIds(Set<String> ids) {
    this.ids = ids;
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

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getAnneeNaissance() {
    return this.anneeNaissance;
  }

  public void setAnneeNaissance(String anneeNaissance) {
    this.anneeNaissance = anneeNaissance;
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

  public CompteSearchRequestDto getCompteSearch() {
    return this.CompteSearch;
  }

  public void setCompteSearch(CompteSearchRequestDto CompteSearch) {
    this.CompteSearch = CompteSearch;
  }

  public AbonnementSearchRequestDto getAbonnementSearch() {
    return this.abonnementSearch;
  }

  public void setAbonnementSearch(AbonnementSearchRequestDto abonnementSearch) {
    this.abonnementSearch = abonnementSearch;
  }

  public List<Criteria> handleSearchParameters() {
    final List<Criteria> listCritarias = new ArrayList<>();

    if (this.getIds() != null && !this.getIds().isEmpty())
      listCritarias.add(Criteria.where("id").in(this.getIds()));

    if (this.getPrenom() != null && !this.getPrenom().isEmpty())
      listCritarias.add(Criteria.where("prenom").regex(".*"+this.getPrenom().trim()+".*","i"));

    if (this.getNom() != null && !this.getNom().isEmpty())
      listCritarias.add(Criteria.where("nom").regex(".*"+this.getNom().trim()+".*","i"));

    if (this.getEmail() != null && !this.getEmail().isEmpty())
      listCritarias.add(Criteria.where("email").regex(".*"+this.getEmail().trim()+".*","i"));

    if (this.getTelephone() != null && !this.getTelephone().isEmpty())
      listCritarias.add(Criteria.where("telephone").regex(".*"+this.getTelephone().trim()+".*","xi"));

    if (this.getEmail() != null && !this.getEmail().isEmpty())
      listCritarias.add(Criteria.where("email").regex(".*"+this.getEmail().trim()+".*","i"));

    return listCritarias;
  }

}
