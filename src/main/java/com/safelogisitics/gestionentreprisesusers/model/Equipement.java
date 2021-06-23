package com.safelogisitics.gestionentreprisesusers.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementType;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "equipements")
public class Equipement {
  
  private String id;

  private EEquipementType type;

  private String libelle;

  private String description;

  private Set<String> specificites = new HashSet<>();

  private Set<HistoriqueFourniture> historiqueFourniture = new HashSet<>();

  private double stock;

  private LocalDateTime dateCreation;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EEquipementType getType() {
    return this.type;
  }

  public void setType(EEquipementType type) {
    this.type = type;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<String> getSpecificites() {
    return this.specificites;
  }

  public void setSpecificites(Set<String> specificites) {
    this.specificites = specificites;
  }

  public Set<HistoriqueFourniture> getHistoriqueFourniture() {
    return this.historiqueFourniture;
  }

  public void setHistoriqueFourniture(Set<HistoriqueFourniture> historiqueFourniture) {
    this.historiqueFourniture = historiqueFourniture;
  }

  public double getStock() {
    return this.stock;
  }

  public void setStock(double stock) {
    this.stock = stock;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

}
