package com.safelogisitics.gestionentreprisesusers.data.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.data.enums.EProspecteurType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETypeProspect;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "prospects")
public class Prospect extends AuditMetadata {

  @Id
  private String id;

  @Field(value = "type")
  private ETypeProspect type;

  @Field(value = "statutProspection")
  private int statutProspection;

  @Field(value = "rapportVisites")
  private Collection<String> rapportVisites;

  @Field(value = "niveauAvancement")
  private int niveauAvancement;

  @Field(value = "infosEntreprise")
  private Entreprise infosEntreprise;

  @Field(value = "infosParticulier")
  private InfosPersoModel infosParticulier;

  @Field(value = "personneRencontrer")
  private Map<String,String> personneRencontrer;

  @Field(value = "prospecteurType")
  private EProspecteurType prospecteurType;

  @Field(value = "prospecteurId")
  private String prospecteurId;

  @Field(value = "dernierEditeurId")
  private String dernierEditeurId;

  @Field(value = "enroleurId")
  private String enroleurId;

  @Field(value = "dateEnrolement")
  private LocalDateTime dateEnrolement;

  @Field(value = "dateDerniereModification")
  private LocalDateTime dateDerniereModification;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public Prospect() {
    this.dateCreation = LocalDateTime.now();
  }

  public Prospect(ETypeProspect type, String prospecteurId, int niveauAvancement, Collection<String> rapportVisites) {
    this.type = type;
    this.prospecteurId = prospecteurId;
    this.niveauAvancement = niveauAvancement;
    this.rapportVisites = rapportVisites;
    this.statutProspection = 0;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ETypeProspect getType() {
    return this.type;
  }

  public void setType(ETypeProspect type) {
    this.type = type;
  }

  public int getStatutProspection() {
    return this.statutProspection;
  }

  public void setStatutProspection(int statutProspection) {
    this.statutProspection = statutProspection;
  }

  public Collection<String> getRapportVisites() {
    return this.rapportVisites;
  }

  public void setRapportVisites(Collection<String> rapportVisites) {
    this.rapportVisites = rapportVisites;
  }

  public int getNiveauAvancement() {
    return this.niveauAvancement;
  }

  public void setNiveauAvancement(int niveauAvancement) {
    this.niveauAvancement = niveauAvancement;
  }

  public Entreprise getInfosEntreprise() {
    return this.infosEntreprise;
  }

  public void setInfosEntreprise(Entreprise infosEntreprise) {
    this.infosEntreprise = infosEntreprise;
  }

  public InfosPersoModel getInfosParticulier() {
    return this.infosParticulier;
  }

  public void setInfosParticulier(InfosPersoModel infosParticulier) {
    this.infosParticulier = infosParticulier;
  }

  public Map<String,String> getPersonneRencontrer() {
		return this.personneRencontrer;
	}

  public void setPersonneRencontrer(Map<String,String> personneRencontrer) {
		this.personneRencontrer = personneRencontrer;
	}

  public EProspecteurType getProspecteurType() {
    return this.prospecteurType;
  }

  public void setProspecteurType(EProspecteurType prospecteurType) {
    this.prospecteurType = prospecteurType;
  }

  public String getProspecteurId() {
    return this.prospecteurId;
  }

  public void setProspecteurId(String prospecteurId) {
    this.prospecteurId = prospecteurId;
  }

  public String getDernierEditeurId() {
    return this.dernierEditeurId;
  }

  public void setDernierEditeurId(String dernierEditeurId) {
    this.dernierEditeurId = dernierEditeurId;
  }

  public String getEnroleurId() {
    return this.enroleurId;
  }

  public void setEnroleurId(String enroleurId) {
    this.enroleurId = enroleurId;
  }

  public LocalDateTime getDateEnrolement() {
    return this.dateEnrolement;
  }

  public void setDateEnrolement(LocalDateTime dateEnrolement) {
    this.dateEnrolement = dateEnrolement;
  }

  public LocalDateTime getDateDerniereModification() {
    return this.dateDerniereModification;
  }

  public void setDateDerniereModification(LocalDateTime dateDerniereModification) {
    this.dateDerniereModification = dateDerniereModification;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }
}
