package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
  
  @NotBlank
  private String username;

  private String numeroEmei;

  @NotBlank
  private String password;

  public LoginRequest() {}

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return this.username.replaceAll("\\s+","");
  }

  public String getNumeroEmei() {
    return this.numeroEmei != null ? this.numeroEmei.replaceAll("\\s+","") : this.numeroEmei;
  }

  public String getPassword() {
    return this.password;
  }
}
