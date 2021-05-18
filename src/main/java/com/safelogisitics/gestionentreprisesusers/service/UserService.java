package com.safelogisitics.gestionentreprisesusers.service;


import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;


public interface UserService {

  public JwtResponse authenticate(LoginRequest loginRequest);
}
