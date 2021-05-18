package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends PagingAndSortingRepository<Role, String> {
  Optional<Role> findByLibelle(String libelle);

  Collection<Role> findByPrivilegesType(ECompteType type);
  
  Boolean existsByLibelle(String libelle);
  
}
