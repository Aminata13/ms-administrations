package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class UpdateInfosPersoRequest extends InfosPersoRequest {
  @NotBlank
  private String username;

  private String password;

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }
}
