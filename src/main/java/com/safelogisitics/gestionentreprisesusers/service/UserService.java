package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.TokenRefreshRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;

public interface UserService {

  public JwtResponse authenticate(LoginRequest loginRequest);

  public JwtResponse refreshToken(TokenRefreshRequest request);

  public User validateCompteUser(String username, String numeroEmei);
}
