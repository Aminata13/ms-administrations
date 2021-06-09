package com.safelogisitics.gestionentreprisesusers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Field(value = "infosPersoId")
  private String infosPersoId;

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

  @JsonIgnore
  @Field(value = "deleted")
  private boolean deleted;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Compte() {
    this.deleted = false;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, String infosPersoId, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.statut = statut;
    this.deleted = false;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, String infosPersoId, Role role, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.role = role;
    this.statut = statut;
    this.deleted = false;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, String infosPersoId, Role role, Set<Service> services, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.role = role;
    this.services = services;
    this.statut = statut;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, Entreprise entreprise, String infosPersoId, Role role, Set<Service> services, int statut) {
    this.type = type;
    this.entreprise = entreprise;
    this.infosPersoId = infosPersoId;
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

  public String getInfosPersoId() {
    return this.infosPersoId;
  }

  public void setInfosPersoId(String infosPersoId) {
    this.infosPersoId = infosPersoId;
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

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Object getCustomRoleFields(String privilegeData) {
    Map <String,Object> _customRoleField = new LinkedHashMap<>();

    _customRoleField.put("id", getId());
    _customRoleField.put("type", getType());
    _customRoleField.put("role", "");
    _customRoleField.put("privileges", new ArrayList<>());
    if (role != null) {
      _customRoleField.put("role", role.getLibelle());
      List<String> privileges = role.getPrivileges().stream().map(
          privilege -> privilegeData.equals("id") ? privilege.getId() : privilege.getValeur().name()
        ).collect(Collectors.toList());
        _customRoleField.put("privileges", privileges);
    }

    return _customRoleField;
  }

  public Object getCustomRoleFields() {
    Map <String,Object> _customRoleField = new LinkedHashMap<>();

    _customRoleField.put("id", getRole().getId());
    _customRoleField.put("type", getType());
    _customRoleField.put("privileges", new ArrayList<>());
    if (role != null) {
      _customRoleField.put("role", role.getLibelle());
      List<String> privileges = role.getPrivileges().stream().map(
        privilege -> privilege.getId()
       ).collect(Collectors.toList());
      _customRoleField.put("privileges", privileges);
    }

    return _customRoleField;
  }

  public int hashCode() {
    return Objects.hash(type, infosPersoId, entreprise, 1000);
  }

  public boolean equals(Object obj) {
    if (obj instanceof Compte) {
        Compte pp = (Compte) obj;
        return (pp.hashCode() == this.hashCode());
    } else {
        return false;
    }
  }
}
