package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.dao.filter.PrivilegeDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.EPrivilege;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDao extends PagingAndSortingRepository<Privilege, String> {
  Optional<Privilege> findByLibelle(String libelle);
  
  Optional<Privilege> findByValeur(String valeur);

  Collection<Privilege> findByType(ECompteType type);

  @Query(value="{ 'type' : ?0 }", fields="{ 'type' : 0}")
  Collection<PrivilegeDefaultFields> findByTypeWithDefaultFields(ECompteType type);

  Collection<Privilege> findByIdInAndType(Set<String> ids, ECompteType type);

  boolean existsByTypeAndValeur(ECompteType type, EPrivilege valeur);  
}
