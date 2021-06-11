package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByInfosPersoId(String infosPersoId);

  Optional<User> findByCurrentAccessToken(String accessToken);

  boolean existsByUsername(String username);

  boolean existsByInfosPersoId(String infosPersoId);
}
