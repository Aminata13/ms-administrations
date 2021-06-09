package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  
  @NotBlank
  private String refreshToken;

  private String numeroEmei;

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getNumeroEmei() {
    return this.numeroEmei;
  }
}
