package com.safelogisitics.gestionentreprisesusers.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${security.jwt.secret}")
	private String jwtSecret;

	@Value("${security.jwt.expiration}")
	private int jwtExpirationMs;

  @Autowired
  private UserDao userDao;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return generateJwtTokenFromUser(userDao.findById(userPrincipal.getId()).get());
	}

  public String generateJwtTokenFromUser(User user) {
    return Jwts.builder()
    .setSubject((user.getUsername()))
    .claim("id", user.getInfosPerso().getId())
    .claim("username", user.getUsername())
    .claim("infosPerso", user.getInfosPerso().getDefaultFields())
    .claim("comptes", user.getInfosPerso().getComptes().stream().map(compte -> compte.getCustomRoleFields("valeur")).collect(Collectors.toList()))
    .setIssuedAt(new Date())
    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
    .signWith(SignatureAlgorithm.HS512, jwtSecret)
    .compact();
  }

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
