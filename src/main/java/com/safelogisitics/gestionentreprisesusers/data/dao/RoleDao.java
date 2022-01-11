package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Role;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends PagingAndSortingRepository<Role, String> {
  Optional<Role> findByLibelle(String libelle);

  Collection<Role> findByTypeAndEditableIsTrue(ECompteType type);
  
  boolean existsByLibelle(String libelle);
}
