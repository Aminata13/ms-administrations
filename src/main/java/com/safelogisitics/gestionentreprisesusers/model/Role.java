package com.safelogisitics.gestionentreprisesusers.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
public class Role {
  
  @Id
  private String id;
  
  @Field(value = "libelle")
  private String libelle;

  @Field(value = "statut")
  private int statut;

  @Field(value = "editable")
  private boolean editable;

  @DBRef
  @Field(value = "privileges")
  private Set<Privilege> privileges = new HashSet<>();

  public Role() {
  }

  public Role(String libelle, int statut) {
    this.libelle = libelle;
    this.statut = statut;
    this.editable = true;
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
  
  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }
  
  public boolean isEditable() {
    return this.editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public Set<Privilege> getPrivileges() {
    return this.privileges;
  }

  public void setPrivileges(Set<Privilege> privileges) {
    this.privileges = privileges;
  }
}
