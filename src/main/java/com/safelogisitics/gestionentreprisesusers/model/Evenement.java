package com.safelogisitics.gestionentreprisesusers.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "evenements")
public class Evenement {

  @Id
  private String id;

  @Field(name = "typeEvenementId")
  private String typeEvenementId;

  @Field(name = "auteurId")
  private String auteurId;

  @Field(name = "titre")
  private String titre;

  @Field(name = "participants")
  private Set<String> participants;

  @Field(name = "autreParticipants")
  private Set<String> autreParticipants;

  @Field(name = "dateDebut")
  private LocalDateTime dateDebut;

  @Field(name = "dateFin")
  private LocalDateTime dateFin;

  @Field(name = "adresse")
  private String adresse;

  @Field(name = "commentaire")
  private String commentaire;

  @Field(name = "rapports")
  private Set<String> rapports;

  @Field(name = "dateCreation")
  private LocalDateTime dateCreation;

  public Evenement() {
    this.dateCreation = LocalDateTime.now();
  }

  public Evenement(
    String typeEvenementId,
    String auteurId,
    String titre,
    Set<String> participants,
    Set<String> autreParticipants,
    LocalDateTime dateDebut,
    LocalDateTime dateFin,
    String adresse,
    String commentaire
  ) {
    this.typeEvenementId = typeEvenementId;
    this.auteurId = auteurId;
    this.titre = titre;
    this.participants = participants;
    this.autreParticipants = autreParticipants;
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    this.adresse = adresse;
    this.commentaire = commentaire;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTypeEvenementId() {
    return this.typeEvenementId;
  }

  public void setTypeEvenementId(String typeEvenementId) {
    this.typeEvenementId = typeEvenementId;
  }

  public String getAuteurId() {
    return this.auteurId;
  }

  public void setAuteurId(String auteurId) {
    this.auteurId = auteurId;
  }

  public String getTitre() {
    return this.titre;
  }

  public void setTitre(String titre) {
    this.titre = titre;
  }

  public Set<String> getParticipants() {
    return this.participants;
  }

  public void setParticipants(Set<String> participants) {
    this.participants = participants;
  }

  public Set<String> getAutreParticipants() {
    return this.autreParticipants;
  }

  public void setAutreParticipants(Set<String> autreParticipants) {
    this.autreParticipants = autreParticipants;
  }

  public LocalDateTime getDateDebut() {
    return this.dateDebut;
  }

  public void setDateDebut(LocalDateTime dateDebut) {
    this.dateDebut = dateDebut;
  }

  public LocalDateTime getDateFin() {
    return this.dateFin;
  }

  public void setDateFin(LocalDateTime dateFin) {
    this.dateFin = dateFin;
  }

  public String getAdresse() {
    return this.adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public String getCommentaire() {
    return this.commentaire;
  }

  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
  }

  public Set<String> getRapports() {
    return this.rapports;
  }

  public void setRapports(Set<String> rapports) {
    this.rapports = rapports;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

}
