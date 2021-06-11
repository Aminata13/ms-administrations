package com.safelogisitics.gestionentreprisesusers.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.RefreshToken;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends PagingAndSortingRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUserId(String userId);

  Optional<RefreshToken> deleteByUserId(String userId);
}