package com.safelogisitics.gestionentreprisesusers.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.safelogisitics.gestionentreprisesusers.data.dao.RefreshTokenDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.model.RefreshToken;
import com.safelogisitics.gestionentreprisesusers.data.model.User;
import com.safelogisitics.gestionentreprisesusers.service.RefreshTokenService;
import com.safelogisitics.gestionentreprisesusers.web.exception.TokenRefreshException;
import com.safelogisitics.gestionentreprisesusers.web.security.jwt.JwtConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl  implements RefreshTokenService {
  
  private final JwtConfig jwtConfig;

  @Autowired
  private RefreshTokenDao refreshTokenDao;

  @Autowired
  private UserDao userDao;

  public RefreshTokenServiceImpl(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenDao.findByToken(token);
  }

  @Override
  public RefreshToken createRefreshToken(String userId) {
    deleteByUserId(userId);

    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(userDao.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(jwtConfig.getRefreshTokenDuration()));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenDao.save(refreshToken);
    return refreshToken;
  }

  @Override
  public Optional<RefreshToken> findByUser(User user) {
    return refreshTokenDao.findByUserId(user.getId());
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenDao.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Override
  @Transactional
  public void deleteByUserId(String userId) {
    try {
      refreshTokenDao.deleteByUserId(userId);
    } catch (Exception e) {
      System.out.println("Error during deleting old refresh token");
    }
  }
}
