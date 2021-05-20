package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByInfosPerso(InfosPerso infosPerso);

  boolean existsByUsername(String username);

  boolean existsByInfosPerso(InfosPerso infosPerso);
}
