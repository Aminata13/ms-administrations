package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  
  @NotBlank
  private String refreshToken;

  private String numeroEmei;

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getNumeroEmei() {
    return this.numeroEmei != null ? this.numeroEmei.trim() : this.numeroEmei;
  }
}
