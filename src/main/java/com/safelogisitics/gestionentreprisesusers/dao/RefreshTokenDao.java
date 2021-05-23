package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.RefreshToken;
import com.safelogisitics.gestionentreprisesusers.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends PagingAndSortingRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> deleteByUser(User user);
}