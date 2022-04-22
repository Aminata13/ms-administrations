package com.safelogisitics.gestionentreprisesusers.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.RoleDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RoleRequest;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Role;
import com.safelogisitics.gestionentreprisesusers.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleDao roleDao;

  public Collection<Role> getRoles() {
    return roleDao.findByTypeAndEditableIsTrue(ECompteType.COMPTE_ADMINISTRATEUR);
  }

  public Collection<Role> getRoles(ECompteType type) {
    return roleDao.findByTypeAndEditableIsTrue(type);
  }

  public Optional<Role> getRoleById(String id) {
    return roleDao.findById(id);
  }

  @Override
  public Role createRole(RoleRequest roleRequest) {
    if (roleDao.existsByLibelle(roleRequest.getLibelle())) {
      throw new IllegalArgumentException("role with that libelle already exists!");
    }

    Role role = new Role(roleRequest.getLibelle());
    role.setPrivilegesActions(roleRequest.getPrivilegesActions());

    role.setType(ECompteType.COMPTE_ADMINISTRATEUR);

    roleDao.save(role);

    return role;
  }

  @Override
  public Collection<Role> createMultiRole(Collection<RoleRequest> roleRequests) {
    Set<Role> roles = new HashSet<>();
    for (RoleRequest roleRequest : roleRequests) {
      Role _role;
      try {
        _role = createRole(roleRequest);
      } catch (Exception e) {
        continue;
      }
      roles.add(_role);
    }
    return roles;
  }

  @Override
  public Role updateRole(String id, RoleRequest roleRequest) {
    Optional<Role> _role = roleDao.findById(id);

    if (!_role.isPresent()) {
      throw new IllegalArgumentException("role not found!");
    }

    Role role = _role.get();

    role.setLibelle(roleRequest.getLibelle());
    role.setPrivilegesActions(roleRequest.getPrivilegesActions());

    roleDao.save(role);

    return role;
  }

  @Override
  public Collection<Role> updateMultiRole(Collection<RoleRequest> roleRequests) {
    Set<Role> roles = new HashSet<>();
    for (RoleRequest roleRequest : roleRequests) {
      Role _role;
      try {
        _role = updateRole(roleRequest.getId(), roleRequest);
      } catch (Exception e) {
        continue;
      }
      roles.add(_role);
    }
    return roles;
  }

  @Override
  public void deleteRole(String id) {
    Optional<Role> _role = roleDao.findById(id);

    if (!_role.isPresent()) {
      throw new IllegalArgumentException("role not found!");
    }

    roleDao.delete(_role.get());
  }

  @Override
  public void deleteMultiRole(Collection<String> ids) {
    for (String id : ids) {
      try {
        deleteRole(id);
      } catch (Exception e) {
        continue;
      }
    }
  }

  @Override
  public Collection<Map<String, Object>> getPrivilegeActions() {
    Collection<Map<String, Object>> listNewPrivileges = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<Collection<Map<String, Object>>> typeReference = new TypeReference<Collection<Map<String, Object>>>(){};

    try {
      listNewPrivileges = mapper.readValue(getClass().getResourceAsStream("/data/administrateur-privileges.json"), typeReference);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
    return listNewPrivileges;
  }

  @Override
  public Collection<Map<String, Object>> getPrivilegeActions(String type) {
    Collection<Map<String, Object>> listNewPrivileges = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<Collection<Map<String, Object>>> typeReference = new TypeReference<Collection<Map<String, Object>>>(){};

    try {
      listNewPrivileges = mapper.readValue(getClass().getResourceAsStream(String.format("/data/%s-privileges.json", type)), typeReference);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
    return listNewPrivileges;
  }
}
