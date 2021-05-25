package com.safelogisitics.gestionentreprisesusers.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

  @Value("${security.jwt.header:Authorization}")
  private String header;

  @Value("${security.jwt.prefix:Bearer }")
  private String prefix;

  @Value("${security.jwt.secret}")
	private String jwtSecret;

	@Value("${security.jwt.expiration}")
	private int jwtExpiration;

  @Value("${security.jwt.refreshExpiration}")
  private Long jwtRefreshTokenDuration;
  
	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

  public String getSecret() {
		return jwtSecret;
	}

	public int getExpiration() {
		return jwtExpiration;
	}

  public Long getRefreshTokenDuration() {
		return jwtRefreshTokenDuration;
	}
}
