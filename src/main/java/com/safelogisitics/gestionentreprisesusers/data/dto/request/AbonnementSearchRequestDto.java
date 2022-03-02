package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Criteria;

public class AbonnementSearchRequestDto {

  private Set<String> ids;
  
  private Set<String> typeAbonnementIds;

  private Set<String> compteClientIds;

  private Set<String> entrepriseIds;

  private Set<String> compteCreateurIds;

  private Set<String> responsableIds;

  private Set<String> numeroCartes;

  private Boolean carteBloquer;

  private Set<Integer> statuts;

  private Boolean deleted;

  private String dateDebut;

  private String dateFin;

  public AbonnementSearchRequestDto() {}

  public Set<String> getIds() {
    return this.ids;
  }

  public void setIds(Set<String> ids) {
    this.ids = ids;
  }

  public Set<String> getTypeAbonnementIds() {
    return this.typeAbonnementIds;
  }

  public void setTypeAbonnementIds(Set<String> typeAbonnementIds) {
    this.typeAbonnementIds = typeAbonnementIds;
  }

  public Set<String> getCompteClientIds() {
    return this.compteClientIds;
  }

  public void setCompteClientIds(Set<String> compteClientIds) {
    this.compteClientIds = compteClientIds;
  }

  public Set<String> getEntrepriseIds() {
    return this.entrepriseIds;
  }

  public void setEntrepriseIds(Set<String> entrepriseIds) {
    this.entrepriseIds = entrepriseIds;
  }

  public Set<String> getCompteCreateurIds() {
    return this.compteCreateurIds;
  }

  public void setCompteCreateurIds(Set<String> compteCreateurIds) {
    this.compteCreateurIds = compteCreateurIds;
  }

  public Set<String> getResponsableIds() {
    return this.responsableIds;
  }

  public void setResponsableIds(Set<String> responsableIds) {
    this.responsableIds = responsableIds;
  }

  public Set<String> getNumeroCartes() {
    return this.numeroCartes;
  }

  public void setNumeroCartes(Set<String> numeroCartes) {
    this.numeroCartes = numeroCartes;
  }

  public Boolean isCarteBloquer() {
    return this.carteBloquer;
  }

  public Boolean getCarteBloquer() {
    return this.carteBloquer;
  }

  public void setCarteBloquer(Boolean carteBloquer) {
    this.carteBloquer = carteBloquer;
  }

  public Set<Integer> getStatuts() {
    return this.statuts;
  }

  public void setStatuts(Set<Integer> statuts) {
    this.statuts = statuts;
  }

  public Boolean isDeleted() {
    return this.deleted;
  }

  public Boolean getDeleted() {
    return this.deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public String getDateDebut() {
    return this.dateDebut;
  }

  public void setDateDebut(String dateDebut) {
    this.dateDebut = dateDebut;
  }

  public String getDateFin() {
    return this.dateFin;
  }

  public void setDateFin(String dateFin) {
    this.dateFin = dateFin;
  }

  public List<Criteria> handleSearchParameters() {
    final List<Criteria> listCritarias = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    if (this.getIds() != null && !this.getIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.id").in(this.getIds()));

    if (this.getTypeAbonnementIds() != null && !this.getTypeAbonnementIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.typeAbonnement.id").in(this.getTypeAbonnementIds()));

    if (this.getCompteClientIds() != null && !this.getCompteClientIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.compteClient.id").in(this.getCompteClientIds()));

    if (this.getEntrepriseIds() != null && !this.getEntrepriseIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.entreprise.id").in(this.getEntrepriseIds()));

    if (this.getCompteCreateurIds() != null && !this.getCompteCreateurIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.compteCreateur.id").in(this.getCompteCreateurIds()));

    if (this.getResponsableIds() != null && !this.getResponsableIds().isEmpty())
      listCritarias.add(Criteria.where("abonnement.responsableId").in(this.getResponsableIds()));

    if (this.getNumeroCartes() != null && !this.getNumeroCartes().isEmpty())
      listCritarias.add(Criteria.where("abonnement.numeroCarte").in(this.getNumeroCartes()));

    if (this.getStatuts() != null && !this.getStatuts().isEmpty())
      listCritarias.add(Criteria.where("abonnement.statut").in(this.getStatuts()));

    if (this.isCarteBloquer() != null)
      listCritarias.add(Criteria.where("abonnement.entrepriseUser").in(this.isCarteBloquer().booleanValue()));

    if (this.getDeleted() != null)
      listCritarias.add(Criteria.where("abonnement.deleted").in(this.getDeleted().booleanValue()));

    if (this.getDateDebut() != null && !this.getDateDebut().isEmpty())
      listCritarias.add(Criteria.where("abonnement.dateCreation").gte(LocalDateTime.parse(this.getDateDebut(), formatter)));

    if (this.getDateFin() != null && !this.getDateFin().isEmpty())
      listCritarias.add(Criteria.where("abonnement.dateCreation").lte(LocalDateTime.parse(this.getDateFin(), formatter)));

    return listCritarias;
  }
}
