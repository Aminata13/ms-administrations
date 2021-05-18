package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comptes")
public class Compte {

  @Id
  private String id;

  @Field(value = "type")
  private ECompteType type;

  @DBRef
  @Field(value = "infosPerso")
  private InfosPerso infosPerso;

  @DBRef
  @Field(value = "entreprise")
  private Entreprise entreprise;

  @DBRef
  @Field(value = "role")
  private Role role;

  @DBRef
  @Field(value = "services")
  private Set<Service> services = new HashSet<>();

  @Field(value = "statut")
  private int statut;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Compte() {
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, InfosPerso infosPerso, Role role, int statut) {
    this.type = type;
    this.infosPerso = infosPerso;
    this.role = role;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, InfosPerso infosPerso, Role role, Set<Service> services, int statut) {
    this.type = type;
    this.infosPerso = infosPerso;
    this.role = role;
    this.services = services;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, Entreprise entreprise, InfosPerso infosPerso, Role role, Set<Service> services, int statut) {
    this.type = type;
    this.entreprise = entreprise;
    this.infosPerso = infosPerso;
    this.role = role;
    this.services = services;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ECompteType getType() {
    return this.type;
  }

  public void setType(ECompteType type) {
    this.type = type;
  }

  public InfosPerso getInfosPerso() {
    return this.infosPerso;
  }

  public void setInfosPerso(InfosPerso infosPerso) {
    this.infosPerso = infosPerso;
  }

  public Entreprise getEntreprise() {
    return this.entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public Role getRole() {
    return this.role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Set<Service> getServices() {
    return this.services;
  }

  public void setServices(Set<Service> services) {
    this.services = services;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }
}
