package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class RoleRequest {
  
  private String id;

  @NotBlank
  private String libelle;

  Set<String> privileges = new HashSet<>();

  Map<String, Set<String>> privilegesActions = new HashMap<>();

  public String getId() {
    return this.id;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public Set<String> getPrivileges() {
    return this.privileges;
  }

  public Map<String, Set<String>> getPrivilegesActions() {
		return this.privilegesActions;
	}
}
