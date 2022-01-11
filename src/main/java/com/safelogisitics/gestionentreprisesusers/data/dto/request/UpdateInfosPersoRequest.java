package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import javax.validation.constraints.NotBlank;

public class UpdateInfosPersoRequest extends InfosPersoRequest {
  @NotBlank
  private String username;

  private String oldPassword;

  private String password;

  public String getUsername() {
    return this.username.replaceAll("\\s+","");
  }

  public String getOldPassword() {
    return this.oldPassword;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }
}
