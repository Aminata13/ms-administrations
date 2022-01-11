package com.safelogisitics.gestionentreprisesusers.data.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "numeroCartes")
public class NumeroCarte extends AuditMetadata {
 
  @Id
  private String id;

  @NotBlank
  @Field(value = "numero")
  private String numero;

  @NotBlank
  @Field(value = "typeAbonnementId")
  private String typeAbonnementId;

  @Field(value = "active")
  private boolean active;

  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public NumeroCarte() {
    this.dateCreation = LocalDateTime.now();
  }

  public NumeroCarte(String numero, String typeAbonnementId, boolean active) {
    this.numero = numero;
    this.typeAbonnementId = typeAbonnementId;
    this.active = active;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getTypeAbonnementId() {
    return this.typeAbonnementId;
  }

  public void setTypeAbonnementId(String typeAbonnementId) {
    this.typeAbonnementId = typeAbonnementId;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

}
