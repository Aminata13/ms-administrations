package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.RefreshToken;
import com.safelogisitics.gestionentreprisesusers.data.model.User;

public interface RefreshTokenService {
  public Optional<RefreshToken> findByToken(String token); // Find a RefreshToken based on the natural id i.e the token itself
  public RefreshToken createRefreshToken(String userId); // Create and return a new Refresh Token
  public RefreshToken verifyExpiration(RefreshToken token); // Verify whether the token provided has expired or not. If the token was expired, delete it from database and throw TokenRefreshException
  public Optional<RefreshToken> findByUser(User user);
  public void deleteByUserId(String userId);
}
