package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  
  @NotBlank
  private String refreshToken;

  public String getRefreshToken() {
    return this.refreshToken;
  }
}
