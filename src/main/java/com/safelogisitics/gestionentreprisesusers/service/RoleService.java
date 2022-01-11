package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.RoleRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Role;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;

public interface RoleService {

  public Collection<Role> getRoles();

  public Collection<Role> getRoles(ECompteType type);

  public Optional<Role> getRoleById(String id);
  
  public Role createRole(RoleRequest roleRequest);

  public Collection<Role> createMultiRole(Collection<RoleRequest> roleRequests);

  public Role updateRole(String id, RoleRequest roleRequest);

  public Collection<Role> updateMultiRole(Collection<RoleRequest> roleRequests);

  public void deleteRole(String id);

  public void deleteMultiRole(Collection<String> ids);

  public Collection<Map<String, Object>> getPrivilegeActions();

  public Collection<Map<String, Object>> getPrivilegeActions(String type);
}
