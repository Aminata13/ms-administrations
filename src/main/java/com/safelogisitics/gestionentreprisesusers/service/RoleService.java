package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.filter.PrivilegeDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.RoleRequest;

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

  public Collection<PrivilegeDefaultFields> getPrivileges();

  public Collection<Privilege> getPrivileges(ECompteType type);

  public Collection<Map<String, Object>> getPrivilegeActions();
}
