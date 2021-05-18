package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.EPrivilege;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDao extends PagingAndSortingRepository<Privilege, String> {
  Optional<Privilege> findByLibelle(String libelle);
  
  Optional<Privilege> findByValeur(String valeur);

  Collection<Privilege> findByType(ECompteType type);

  Boolean existsByTypeAndValeur(ECompteType type, EPrivilege valeur);  
}
