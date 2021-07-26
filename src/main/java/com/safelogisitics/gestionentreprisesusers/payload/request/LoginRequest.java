package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
  
  @NotBlank
  private String username;

  private String numeroEmei;

  @NotBlank
  private String password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return this.username.replaceAll("\\D+","");
  }

  public String getNumeroEmei() {
    return this.numeroEmei.replaceAll("\\D+","");
  }

  public String getPassword() {
    return this.password;
  }
}
