package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.model.SMSModel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSModelDao extends PagingAndSortingRepository<SMSModel, String> {
  Optional<SMSModel> findBySubjectIgnoreCase(String subject);

  Optional<SMSModel> findByMotCleIgnoreCase(String motCle);

  Collection<SMSModel> findByCibleIn(Set<String> cibles);

  boolean existsBySubjectIgnoreCase(String subject);
}
