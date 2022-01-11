package com.safelogisitics.gestionentreprisesusers.data.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
public class Role extends AuditMetadata {
  
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
