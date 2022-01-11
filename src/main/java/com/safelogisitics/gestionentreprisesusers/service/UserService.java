package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.TokenRefreshRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.data.model.User;

public interface UserService {

  public JwtResponse authenticate(LoginRequest loginRequest);

  public void logout();

  public JwtResponse refreshToken(TokenRefreshRequest request);

  public User validateCompteUser(String username, String numeroEmei);
}
