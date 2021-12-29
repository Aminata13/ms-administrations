package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "typeAbonnements")
public class TypeAbonnement extends AuditMetadata {
  
  @Id
  private String id;

  @Field(value = "libelle")
  private String libelle;

  @Field(value = "icon")
  private String icon;

  @Field(value = "reduction")
  private int reduction;

  @Field(value = "prix")
  private BigDecimal prix;

  @Field
  private Set<EServiceType> services;

  @Field(value = "statut")
  private int statut;

  public TypeAbonnement() {}

  public TypeAbonnement(String libelle, int reduction, int statut) {
    this.libelle = libelle;
    this.reduction = reduction;
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

  public String getIcon() {
    return this.icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public int getReduction() {
    return this.reduction;
  }

  public void setReduction(int reduction) {
    this.reduction = reduction;
  }

  public BigDecimal getPrix() {
    return this.prix;
  }

  public void setPrix(BigDecimal prix) {
    this.prix = prix;
  }

  public Set<EServiceType> getServices() {
    return services;
  }

  public void setServices(Set<EServiceType> services) {
    this.services = services;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

}
