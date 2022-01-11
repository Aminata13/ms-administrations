package com.safelogisitics.gestionentreprisesusers.data.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.annotations.NotNull;
import com.safelogisitics.gestionentreprisesusers.data.enums.EEquipementCategorie;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "equipements")
public class Equipement extends AuditMetadata {

  private String id;

  @NotNull
  private Set<EEquipementCategorie> categories;

  @NotBlank
  private String libelle;

  private String description;

  private Set<EquipementSpecificites> specificites = new HashSet<>();

  @JsonIgnore
  private Set<FournitureEquipement> historiqueFournitures = new HashSet<>();

  private double stock;

  private double quantiteAffecter;

  private LocalDateTime dateCreation;

  public Equipement() {
    this.quantiteAffecter = 0;
    this.dateCreation = LocalDateTime.now();
  }

  public Equipement(String libelle, Set<EEquipementCategorie> categories, String description, Set<EquipementSpecificites> specificites) {
    this.libelle = libelle;
    this.categories = categories;
    this.description = description;
    this.specificites = specificites;
    this.quantiteAffecter = 0;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<EEquipementCategorie> getCategories() {
    return this.categories;
  }

  public void setCategories(Set<EEquipementCategorie> categories) {
    this.categories = categories;
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

  public Set<EquipementSpecificites> getSpecificites() {
    return this.specificites;
  }

  public void setSpecificites(Set<EquipementSpecificites> specificites) {
    this.specificites = specificites;
  }

  public Set<FournitureEquipement> getHistoriqueFournitures() {
    return this.historiqueFournitures;
  }

  public void addHistoriqueFourniture(FournitureEquipement historiqueFourniture) {
    this.historiqueFournitures.add(historiqueFourniture);
  }

  public double getStock() {
    return this.stock;
  }

  public void setStock(double stock) {
    this.stock = stock;
  }

  public double getQuantiteAffecter() {
    return this.quantiteAffecter;
  }

  public void setQuantiteAffecter(double quantiteAffecter) {
    this.quantiteAffecter = quantiteAffecter;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }

}
