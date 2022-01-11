package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.model.TypeEvenement;

public class EvenementDto {
  
  private String id;

  private Optional<TypeEvenement> typeEvenement;

  private String auteurId;

  private String titre;

  private Set<String> participants;

  private Set<String> autreParticipants;

  private LocalDateTime dateDebut;

  private LocalDateTime dateFin;

  private String adresse;

  private String commentaire;

  private Set<String> rapports;

  private LocalDateTime dateCreation;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<TypeEvenement> getTypeEvenement() {
    return this.typeEvenement;
  }

  public void setTypeEvenement(Optional<TypeEvenement> typeEvenement) {
    this.typeEvenement = typeEvenement;
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
