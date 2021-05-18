package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.dao.PrivilegeDao;
import com.safelogisitics.gestionentreprisesusers.dao.RoleDao;
import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.RoleRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  
  @Autowired
  private PrivilegeDao privilegeDao;

  @Autowired
  private RoleDao roleDao;

  @Override
  public Role createRole(RoleRequest roleRequest) {
    if (roleDao.existsByLibelle(roleRequest.getLibelle())) {
      throw new IllegalArgumentException("role with that libelle already exists!");
    }

    Role role = new Role(roleRequest.getLibelle());
    Set<Privilege> privileges = (Set<Privilege>) privilegeDao.findByIdInAndType(roleRequest.getPrivileges(), ECompteType.COMPTE_ADMINISTRATEUR);

    role.setPrivileges(privileges);

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
    Set<Privilege> privileges = (Set<Privilege>) privilegeDao.findByIdInAndType(roleRequest.getPrivileges(), ECompteType.COMPTE_ADMINISTRATEUR);

    role.setPrivileges(privileges);

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
  public void delete(String id) {
    Optional<Role> _role = roleDao.findById(id);

    if (!_role.isPresent()) {
      throw new IllegalArgumentException("role not found!");
    }

    roleDao.delete(_role.get());
  }

  @Override
  public void deleteMultiple(Collection<String> ids) {
    for (String id : ids) {
      try {
        delete(id);
      } catch (Exception e) {
        continue;
      }
    }
  }

  public Collection<Privilege> getPrivileges() {
    return privilegeDao.findByType(ECompteType.COMPTE_ADMINISTRATEUR);
  }

  public Collection<Privilege> getPrivileges(ECompteType type) {
    return privilegeDao.findByType(type);
  }
}
