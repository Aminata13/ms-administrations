package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

import org.springframework.data.mongodb.core.query.Criteria;

public class CompteSearchRequestDto {

  private Set<String> ids;

  private Set<ECompteType> types;

  private Set<String> infosPersoIds;

  private Set<String> entrepriseIds;

  private Boolean entrepriseUser;

  private Set<String> roleIds;

  private Set<EServiceConciergeType> serviceConciergeries;

  private Set<EServiceType> services;

  private Set<String> moyenTransportIds;

  private String numeroEmei;

  private String numeroReference;

  private Set<Integer> statuts;

  private Boolean deleted;

  private String dateDebut;

  private String dateFin;

  public CompteSearchRequestDto() {}

  public Set<String> getIds() {
    return this.ids;
  }

  public void setIds(Set<String> ids) {
    this.ids = ids;
  }

  public Set<ECompteType> getTypes() {
    return this.types;
  }

  public void setTypes(Set<ECompteType> types) {
    this.types = types;
  }

  public Set<String> getInfosPersoIds() {
    return this.infosPersoIds;
  }

  public void setInfosPersoIds(Set<String> infosPersoIds) {
    this.infosPersoIds = infosPersoIds;
  }

  public Set<String> getEntrepriseIds() {
    return this.entrepriseIds;
  }

  public void setEntrepriseIds(Set<String> entrepriseIds) {
    this.entrepriseIds = entrepriseIds;
  }

  public Boolean isEntrepriseUser() {
    return this.entrepriseUser;
  }

  public Boolean getEntrepriseUser() {
    return this.entrepriseUser;
  }

  public void setEntrepriseUser(Boolean entrepriseUser) {
    this.entrepriseUser = entrepriseUser;
  }

  public Set<String> getRoleIds() {
    return this.roleIds;
  }

  public void setRoleIds(Set<String> roleIds) {
    this.roleIds = roleIds;
  }

  public Set<EServiceConciergeType> getServiceConciergeries() {
    return this.serviceConciergeries;
  }

  public void setServiceConciergeries(Set<EServiceConciergeType> serviceConciergeries) {
    this.serviceConciergeries = serviceConciergeries;
  }

  public Set<EServiceType> getServices() {
    return this.services;
  }

  public void setServices(Set<EServiceType> services) {
    this.services = services;
  }

  public Set<String> getMoyenTransportIds() {
    return this.moyenTransportIds;
  }

  public void setMoyenTransportIds(Set<String> moyenTransportIds) {
    this.moyenTransportIds = moyenTransportIds;
  }

  public String getNumeroEmei() {
    return this.numeroEmei;
  }

  public void setNumeroEmei(String numeroEmei) {
    this.numeroEmei = numeroEmei;
  }

  public String getNumeroReference() {
    return this.numeroReference;
  }

  public void setNumeroReference(String numeroReference) {
    this.numeroReference = numeroReference;
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
      listCritarias.add(Criteria.where("compte.id").in(this.getIds()));

    if (this.getTypes() != null && !this.getTypes().isEmpty())
      listCritarias.add(Criteria.where("compte.type").in(this.getTypes()));

    if (this.getInfosPersoIds() != null && !this.getInfosPersoIds().isEmpty())
      listCritarias.add(Criteria.where("compte.infosPersoId").in(this.getInfosPersoIds()));

    if (this.getEntrepriseIds() != null && !this.getEntrepriseIds().isEmpty())
      listCritarias.add(Criteria.where("compte.entrepriseId").in(this.getEntrepriseIds()));

    if (this.getRoleIds() != null && !this.getRoleIds().isEmpty())
      listCritarias.add(Criteria.where("compte.role.id").in(this.getRoleIds()));

    if (this.getServiceConciergeries() != null && !this.getServiceConciergeries().isEmpty())
      listCritarias.add(Criteria.where("compte.serviceConciergeries").in(this.getServiceConciergeries()));

    if (this.getServices() != null && !this.getServices().isEmpty())
      listCritarias.add(Criteria.where("compte.services").in(this.getServices()));

    if (this.getMoyenTransportIds() != null && !this.getMoyenTransportIds().isEmpty())
      listCritarias.add(Criteria.where("compte.moyenTransportId").in(this.getMoyenTransportIds()));

    if (this.getStatuts() != null && !this.getStatuts().isEmpty())
      listCritarias.add(Criteria.where("compte.statut").in(this.getStatuts()));
    else
      listCritarias.add(Criteria.where("compte.statut").ne(-1));

    if (this.getTypes() != null && !this.getTypes().isEmpty())
      listCritarias.add(Criteria.where("compte.type").in(this.getTypes()));

    if (this.getNumeroEmei() != null && !this.getNumeroEmei().isEmpty())
      listCritarias.add(Criteria.where("compte.numeroEmei").regex(".*"+this.getNumeroEmei().trim()+".*","i"));

    if (this.getNumeroReference() != null && !this.getNumeroReference().isEmpty())
      listCritarias.add(Criteria.where("compte.numeroReference").regex(".*"+this.getNumeroReference().trim()+".*","i"));

    if (this.isEntrepriseUser() != null)
      listCritarias.add(Criteria.where("compte.entrepriseUser").in(this.isEntrepriseUser().booleanValue()));

    if (this.isDeleted() != null)
      listCritarias.add(Criteria.where("compte.deleted").in(this.isDeleted().booleanValue()));
    else
      listCritarias.add(Criteria.where("compte.deleted").is(false));

    if (this.getDateDebut() != null && !this.getDateDebut().isEmpty())
      listCritarias.add(Criteria.where("compte.dateCreation").gte(LocalDateTime.parse(this.getDateDebut(), formatter)));

    if (this.getDateFin() != null && !this.getDateFin().isEmpty())
      listCritarias.add(Criteria.where("compte.dateCreation").lte(LocalDateTime.parse(this.getDateFin(), formatter)));

    return listCritarias;
  }
}
