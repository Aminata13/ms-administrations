package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
  
  @NotBlank
  private String username;

  @NotBlank
  private String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }
}
