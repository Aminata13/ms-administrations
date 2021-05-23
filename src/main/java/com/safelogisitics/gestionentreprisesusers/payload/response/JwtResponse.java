package com.safelogisitics.gestionentreprisesusers.payload.response;

public class JwtResponse {
	private String token;

  private String refreshToken;

	private String type = "Bearer";

	public JwtResponse(String accessToken, String refreshToken) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

  public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}
}
