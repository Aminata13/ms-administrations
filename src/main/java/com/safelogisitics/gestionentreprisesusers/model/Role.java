package com.safelogisitics.gestionentreprisesusers.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.EPrivilege;

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

  @JsonIgnore
  @Field(value = "editable")
  private boolean editable;

  @JsonIgnore
  @Field(value = "type")
  private ECompteType type;

  @DBRef
  @Field(value = "privileges")
  private Set<Privilege> privileges = new HashSet<>();

  @Field(value = "privilegesActions")
  private Map<String, Set<String>> privilegesActions = new HashMap<>();

  public Role() {
  }

  public Role(String libelle) {
    this.libelle = libelle;
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
  
  public boolean isEditable() {
    return this.editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public ECompteType getType() {
    return this.type;
  }

  public void setType(ECompteType type) {
    this.type = type;
  }

  public Set<Privilege> getPrivileges() {
    return this.privileges;
  }

  public void setPrivileges(Set<Privilege> privileges) {
    this.privileges = privileges;
  }

  public boolean hasPrivilege(EPrivilege _privilege) {
    for (Privilege privilege : privileges) {
      if (privilege.getValeur().equals(_privilege)) {
        return true;
      }
    }
    return false;
  }

  public Map<String, Set<String>> getPrivilegesActions() {
		return this.privilegesActions;
	}

	public void setPrivilegesActions(Map<String, Set<String>> privilegesActions) {
		this.privilegesActions = privilegesActions;
	}

  public boolean hasPrivilegeAction(String _privilege) {
    for (String privilege : this.privilegesActions.keySet()) {
      if (privilege.equals(_privilege)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasPrivilegeAction(String _privilege, String action) {
    for (String privilege : this.privilegesActions.keySet()) {
      if (privilege.equals(_privilege) && this.privilegesActions.get(_privilege).contains(action)) {
        return true;
      }
    }
    return false;
  }
}
