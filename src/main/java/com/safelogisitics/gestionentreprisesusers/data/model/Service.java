package com.safelogisitics.gestionentreprisesusers.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "services")
public class Service extends AuditMetadata {
  
  @Id
  private String id;
  
  @Field(value = "libelle")
  private String libelle;

  @Field(value = "publicService")
  private boolean publicService;

  @Field(value = "statut")
  private int statut;

  public Service() {
  }

  public Service(String libelle, int statut) {
    this.libelle = libelle;
    this.statut = statut;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public boolean isPublicService() {
    return this.publicService;
  }

  public void setPublicService(boolean publicService) {
    this.publicService = publicService;
  }
  
  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }
}
