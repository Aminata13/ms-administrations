package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;

import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.RoleRequest;

public interface RoleService {
  
  public Role createRole(RoleRequest roleRequest);

  public Collection<Role> createMultiRole(Collection<RoleRequest> roleRequests);

  public Role updateRole(String id, RoleRequest roleRequest);

  public Collection<Role> updateMultiRole(Collection<RoleRequest> roleRequests);

  public void delete(String id);

  public void deleteMultiple(Collection<String> ids);

  public Collection<Privilege> getPrivileges();

  public Collection<Privilege> getPrivileges(ECompteType type);
}
